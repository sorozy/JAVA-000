package com.task.java.week08.shardingsphereshardingdemo.service;

import com.task.java.week08.shardingsphereshardingdemo.entity.User;

import java.util.List;

/**
 * @Author lgd
 * @Date 2020/12/9 上午9:40
 * @Version 1.0
 **/
public interface UserService {

	public User insert(User user);

	public List<User> listAll();

	public void delete(Long id);
}
