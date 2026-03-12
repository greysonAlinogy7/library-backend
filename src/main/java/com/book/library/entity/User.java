package com.book.library.entity;

import com.book.library.domain.AuthProvider;
import com.book.library.domain.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private  String phone;
    private AuthProvider authProvider = AuthProvider.LOCAL;
    private String googleId;
    private String profileImage;
    private String password;
    private LocalDateTime lastLogin;
    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
