package com.task.java.week08.shardingsphereshardingdemo.service;

/**
 * @Author lgd
 * @Date 2020/12/9 上午10:07
 * @Version 1.0
 **/
public interface DistributedTransactionService {


	public String transactionSuccessTest(Long goodsId, Long buyerId, Long userId);


	public String transactionExceptionTest(Long goodsId, Long buyerId, Long userId);
}
