package com.book.library.service.impl;

import com.book.library.entity.User;
import com.book.library.payload.dto.UserDTO;

import java.util.List;

public interface IUserService {
   public User getCurrentUser() throws Exception;
   public List<UserDTO> getAllUsers();
   User findById(Long id) throws Exception;
}
