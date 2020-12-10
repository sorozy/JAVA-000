package com.task.java.week08.shardingsphereshardingdemo.controller;

import com.task.java.week08.shardingsphereshardingdemo.entity.Goods;
import com.task.java.week08.shardingsphereshardingdemo.entity.User;
import com.task.java.week08.shardingsphereshardingdemo.service.GoodsService;
import com.task.java.week08.shardingsphereshardingdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @Author lgd
 * @Date 2020/12/8 上午10:23
 * @Version 1.0
 **/

@Controller
@RequestMapping(value = "/shardingsphere/user/")
public class UserController {


	@Autowired
	private UserService userService;

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	@ResponseBody
	public String demo(){
		return "hello world";
	}


	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	@ResponseBody
	public String insert(Long id){


		User insertableUser = new User();
		insertableUser.setId(id);
		insertableUser.setEmail("wayne@163.com");
		insertableUser.setIdCard(String.valueOf(id));
		insertableUser.setNickname("wayne");
		insertableUser.setPassport("12345Abc");
		insertableUser.setUserName("wayne");
		long now  = System.currentTimeMillis();
		insertableUser.setCreatedAt(now);
		insertableUser.setUpdatedAt(now);
		userService.insert(insertableUser);

		return insertableUser.toString();
	}

	@RequestMapping(value = "/selects", method = RequestMethod.GET)
	@ResponseBody
	public List<User> select(){

		List<User> userList =  userService.listAll();
		return userList;
	}

}
