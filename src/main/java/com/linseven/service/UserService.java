package com.linseven.service;

import com.linseven.model.UserPO;

public interface UserService {

     void addUser(UserPO userPO) throws Exception;
     UserPO findUser(String username);
     void updateUser(UserPO userPO) throws Exception;

}
