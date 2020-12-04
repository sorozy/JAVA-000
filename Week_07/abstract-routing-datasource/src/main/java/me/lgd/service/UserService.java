package me.lgd.service;

import me.lgd.entity.User;

import java.util.List;

/**
 * @author lgd
 * @date 2020/12/1 21:44
 */
public interface UserService {

    List<User> listUser();

    void update(User user);

    User findById(Integer id);
}
