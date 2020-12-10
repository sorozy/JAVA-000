package com.task.java.week08.shardingsphereshardingdemo.service;

import com.task.java.week08.shardingsphereshardingdemo.entity.Goods;

import java.util.List;

/**
 * @Author lgd
 * @Date 2020/12/8 上午10:29
 * @Version 1.0
 **/
public interface GoodsService {

	public Goods insert(Goods goods);

	public List<Goods> listAll();

	public void delete(Long goodsId, Long buyerId);
}
