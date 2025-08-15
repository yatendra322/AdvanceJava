package com.main.user.services.services;

import com.main.user.services.entities.User;

import java.util.List;

public interface UserService {

    //create
    User saveUser (User user);

    //Get all user
    List<User> getAllUser();

    //Get Single user of given userId
    User getUser(String userId);

    //Delete user
    boolean deleteUser(String userId);

    //Update user
    User updateUser(User user);

}
