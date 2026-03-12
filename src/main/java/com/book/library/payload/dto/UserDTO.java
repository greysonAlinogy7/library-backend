package com.book.library.payload.dto;

import com.book.library.domain.AuthProvider;
import com.book.library.domain.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotNull(message = "email is required")
    private String email;
    @NotNull(message = "password is required")
    private String password;
    private  String phone;
    @NotNull(message = "fullName is required")
    private String fullName;
    private UserRole role;
    private String username;
    private LocalDateTime lastLogin;

}
