package com.task.java.week08.shardingsphereshardingdemo.controller;

import com.task.java.week08.shardingsphereshardingdemo.service.DistributedTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author lgd
 * @Date 2020/12/9 上午10:15
 * @Version 1.0
 **/

@Controller
@RequestMapping(value = "/shardingsphere/dt/")
public class DistributedTransactionsController {


	@Autowired
	private DistributedTransactionService distributedTransactionService;

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	@ResponseBody
	public String demo(){
		return "hello DistributedTransactionsController";
	}


	@RequestMapping(value = "/success", method = RequestMethod.GET)
	@ResponseBody
	public String success(Long goodsId, Long buyerId, Long userId){

		return distributedTransactionService.transactionSuccessTest(goodsId, buyerId, userId);
	}


	@RequestMapping(value = "/exception", method = RequestMethod.GET)
	@ResponseBody
	public String exception(Long goodsId, Long buyerId, Long userId){

		return distributedTransactionService.transactionExceptionTest(goodsId, buyerId, userId);
	}
}
