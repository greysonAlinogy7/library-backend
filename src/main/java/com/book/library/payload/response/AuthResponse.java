package com.book.library.payload.response;


import com.book.library.payload.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private  String jwt;
    private String message;
    private String title;
    private UserDTO user;
}
