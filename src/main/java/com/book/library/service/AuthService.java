package com.book.library.service;

import com.book.library.config.JwtProvider;
import com.book.library.domain.PasswordResetToken;
import com.book.library.domain.UserRole;
import com.book.library.entity.User;
import com.book.library.exception.UserException;
import com.book.library.mappers.UserMapper;
import com.book.library.payload.dto.UserDTO;
import com.book.library.payload.response.AuthResponse;
import com.book.library.repository.PasswordResetTokenRepository;
import com.book.library.repository.UserRepository;
import com.book.library.service.impl.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    
    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtProvider jwtProvider;
    private  final CustomUserServiceImplimentation customUserServiceImplimentation;
    private  final PasswordResetTokenRepository passwordResetTokenRepository;
    private  final EmailService emailService;





    @Override
    public AuthResponse login(String username, String password) throws UserException {
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        User user = userRepository.findByEmail(username);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setJwt(token);
        response.setTitle("Welcome" + user.getFullName());
        response.setMessage("successfully logged in");
        response.setUser(UserMapper.toDTO(user));

        return response;
    }



    private Authentication  authenticate(String username, String password) throws UserException {
        UserDetails userDetails= customUserServiceImplimentation.loadUserByUsername(username);
        if (userDetails==null){
            throw  new UserException("user not found with email" + password);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw  new UserException("password not match");
        }
        return  new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

    }



    @Override
    public AuthResponse signup(UserDTO request) throws UserException {
        User user=userRepository.findByEmail(request.getEmail());
        if (user!=null){
            throw  new UserException("email is already registered");
        }
        User createdUser = new User();
        createdUser.setEmail(request.getEmail());
        createdUser.setPassword(passwordEncoder.encode(request.getPassword()));
        createdUser.setFullName(request.getFullName());
        createdUser.setLastLogin(LocalDateTime.now());
        createdUser.setRole(UserRole.ROLE_USER);
        User savedUser = userRepository.save(createdUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtProvider.generateToken(auth);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setTitle("Welcome"+createdUser.getFullName());
        response.setMessage("successfully registered");
        response.setUser(UserMapper.toDTO(savedUser));
        return response;
    }



    @Transactional
    public void createPasswordResetToken(String email) throws UserException {

        String frontendUrl = "";
        User user = userRepository.findByEmail(email);
        if (user==null){
            throw  new UserException("user not found with given email");
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        passwordResetTokenRepository.save(resetToken);
        String resetLink=frontendUrl+token;
        String subject="Password Reset Request";
        String body = "You requested  to reset your password. user this link (valid 5 minutes)";


        emailService.sendEmail(user.getEmail(), subject, body);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new Exception("token not valid"));
        if (resetToken.isExpired()){
            throw  new Exception("token expired");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);

    }
}
