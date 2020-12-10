package com.task.java.week08.shardingsphereshardingdemo.service.Impl;

import com.task.java.week08.shardingsphereshardingdemo.entity.Goods;
import com.task.java.week08.shardingsphereshardingdemo.entity.User;
import com.task.java.week08.shardingsphereshardingdemo.service.DistributedTransactionService;
import com.task.java.week08.shardingsphereshardingdemo.service.GoodsService;
import com.task.java.week08.shardingsphereshardingdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTAC;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author lgd
 * @Date 2020/12/9 上午10:08
 * @Version 1.0
 **/

@Service
@Slf4j
public class DistributedTransactionServiceImpl implements DistributedTransactionService {

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private UserService userService;

	@Override
	@HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
//	@HmilyTAC
	public String transactionSuccessTest(Long goodsId, Long buyerId, Long userId) {

		Goods insertableGoods = new Goods();
		insertableGoods.setId(goodsId);
		insertableGoods.setBuyerId(buyerId);
		insertableGoods.setGoodsName("围巾");
		insertableGoods.setSellerId(10086L);
		insertableGoods.setFirstCategory(1);
		insertableGoods.setSecondCategory(2);
		insertableGoods.setWeight(new BigDecimal("200.2"));
		insertableGoods.setUnit((byte)1);
		insertableGoods.setPrice(new BigDecimal("15"));
		long now = System.currentTimeMillis();
		insertableGoods.setCreatedAt(now);
		insertableGoods.setUpdatedAt(now);
		goodsService.insert(insertableGoods);


		User insertableUser = new User();
		insertableUser.setId(userId);
		insertableUser.setEmail("wayne@163.com");
		insertableUser.setIdCard(String.valueOf(userId));
		insertableUser.setNickname("wayne");
		insertableUser.setPassport("12345Abc");
		insertableUser.setUserName("wayne");
		insertableUser.setCreatedAt(now);
		insertableUser.setUpdatedAt(now);
		userService.insert(insertableUser);

		return "success";
	}

	@Override
	@HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
//	@HmilyTAC
	public String transactionExceptionTest(Long goodsId, Long buyerId, Long userId) {

		Goods insertableGoods = new Goods();
		insertableGoods.setId(goodsId);
		insertableGoods.setBuyerId(buyerId);
		insertableGoods.setGoodsName("围巾");
		insertableGoods.setSellerId(10086L);
		insertableGoods.setFirstCategory(1);
		insertableGoods.setSecondCategory(2);
		insertableGoods.setWeight(new BigDecimal("200.2"));
		insertableGoods.setUnit((byte)1);
		insertableGoods.setPrice(new BigDecimal("15"));
		long now = System.currentTimeMillis();
		insertableGoods.setCreatedAt(now);
		insertableGoods.setUpdatedAt(now);
		goodsService.insert(insertableGoods);


		User insertableUser = new User();
		insertableUser.setId(userId);
		insertableUser.setEmail("wayne@163.com");
		insertableUser.setIdCard(String.valueOf(userId));
		insertableUser.setNickname("wayne");
		insertableUser.setPassport("12345Abc");
		insertableUser.setUserName("wayne");
		insertableUser.setCreatedAt(now);
		insertableUser.setUpdatedAt(now);
		userService.insert(insertableUser);
		throw new RuntimeException("插入用户表抛异常了！！！");

	}


	public void confirm(Long goodsId, Long buyerId, Long userId) {
		log.info("=========事务confirm操作完成================");
	}

	public void cancel(Long goodsId, Long buyerId, Long userId) {
		userService.delete(userId);
		goodsService.delete(goodsId, buyerId);
		log.info("=========事务cancel操作完成================");
	}


}
