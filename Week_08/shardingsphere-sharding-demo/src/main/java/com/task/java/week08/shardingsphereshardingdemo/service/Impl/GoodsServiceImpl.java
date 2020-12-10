package com.task.java.week08.shardingsphereshardingdemo.service.Impl;

import com.task.java.week08.shardingsphereshardingdemo.entity.Goods;
import com.task.java.week08.shardingsphereshardingdemo.entity.GoodsExample;
import com.task.java.week08.shardingsphereshardingdemo.mapper.GoodsMapper;
import com.task.java.week08.shardingsphereshardingdemo.service.GoodsService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lgd
 * @Date 2020/12/8 上午10:30
 * @Version 1.0
 **/

@Service
public class GoodsServiceImpl implements GoodsService {

	@SuppressWarnings("all")
	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public Goods insert(Goods goods) {

		goodsMapper.insert(goods);
		return goods;
	}

	@Override
	public List<Goods> listAll() {

		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria();
		List<Goods> goodsList = goodsMapper.selectByExample(goodsExample);
		return goodsList;
	}

	@Override
	public void delete(Long goodsId, Long buyerId) {
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andBuyerIdEqualTo(buyerId).andIdEqualTo(goodsId);
		goodsMapper.deleteByExample(goodsExample);
	}
}
