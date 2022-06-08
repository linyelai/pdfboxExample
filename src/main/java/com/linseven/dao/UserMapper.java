package com.linseven.dao;

import com.linseven.model.UserPO;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    /**
     * find by username
     * @param username
     * @return
     */
     UserPO findUser(@Param("username") String username);

    /**
     *  add user
     * @param userPO
     */
    void addUser(UserPO userPO);

    void updateUser(UserPO userPO);
}
