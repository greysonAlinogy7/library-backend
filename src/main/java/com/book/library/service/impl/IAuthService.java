package com.book.library.service.impl;

import com.book.library.exception.UserException;
import com.book.library.payload.dto.UserDTO;
import com.book.library.payload.response.AuthResponse;

public interface IAuthService {
    AuthResponse login(String username , String password) throws UserException;
    AuthResponse signup(UserDTO request) throws UserException;
    void createPasswordResetToken(String email) throws UserException;
    void resetPassword(String token, String newPassword) throws Exception;

}
