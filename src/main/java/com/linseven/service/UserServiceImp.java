package com.linseven.service;

import com.linseven.dao.UserMapper;
import com.linseven.model.UserPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/6/8 11:49
 */
@Service
@Slf4j
public class UserServiceImp implements  UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(UserPO userPO) throws Exception {
       userMapper.addUser(userPO);
       UserPO userPO1 = new UserPO();
       userPO1.setUsername("test");
       userPO1.setPassword("654321");
       updateUser(userPO1);
    }

    @Override
    public UserPO findUser(String username) {
        return userMapper.findUser(username);
    }


    @Transactional(rollbackFor = Exception.class,propagation = Propagation.NEVER)
    @Override
    public void updateUser(UserPO userPO) throws Exception {

        log.info("update user:{}",userPO);
        userMapper.updateUser(userPO);
        throw new Exception();
    }
}
