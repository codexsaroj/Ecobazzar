package com.ecobazzar.service.interf;

import com.ecobazzar.dto.LoginRequest;
import com.ecobazzar.dto.Response;
import com.ecobazzar.dto.UserDto;
import com.ecobazzar.entity.User;

public interface UserService {
    Response registerUser(UserDto registrationRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getLoginUser();
    Response getUserInfoAndOrderHistory();
}
