package com.nbu.graduation.spring.service;

import com.nbu.graduation.spring.model.enums.Role;
import com.nbu.graduation.spring.model.service.UserServiceModel;

import java.util.List;

public interface UserService {
    UserServiceModel createUser(UserServiceModel userServiceModel);

    UserServiceModel updateUser(UserServiceModel userServiceModel);

    UserServiceModel getUserById(Long id);

    void deleteUserById(Long id);

    UserServiceModel findByUsername(String username);

    List<UserServiceModel> findAllByRole(Role role);

    boolean usernameOrEmailExists(String username, String email);

    void initializeUsers();
}
