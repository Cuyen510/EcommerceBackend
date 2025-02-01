package com.example.shopApp_backend.services.UserService;

import com.example.shopApp_backend.dtos.UserDTO;
import com.example.shopApp_backend.exceptions.DataNotFoundException;
import com.example.shopApp_backend.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password, Long roleId) throws DataNotFoundException;

    User getUserDetailsFromToken(String token) throws Exception;


}
