package com.book.library.controller;

import com.book.library.exception.UserException;
import com.book.library.payload.dto.UserDTO;
import com.book.library.payload.request.LoginRequest;
import com.book.library.payload.request.ResetPasswordRequest;
import com.book.library.payload.response.ApiResponse;
import com.book.library.payload.response.AuthResponse;
import com.book.library.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private  final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUpHandler( @RequestBody UserDTO request) throws UserException {
    AuthResponse res = authService.signup(request);
     return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@Valid @RequestBody  LoginRequest request) throws UserException {
        AuthResponse res = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) throws UserException, Exception {
         authService.resetPassword(request.getToken(), request.getPassword());
        ApiResponse res = new ApiResponse("password reset successfully", true);
        return ResponseEntity.ok(res);
    }


}
