package com.linseven.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tyrion
 * @version 1.0
 * @date 2022/6/8 11:50
 */
@Data
public class UserPO {
    int id;
    private String username;
    private String password;
}
