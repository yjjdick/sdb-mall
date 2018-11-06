package io.sdb.dao;

import io.sdb.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(User.class);
    }
}
