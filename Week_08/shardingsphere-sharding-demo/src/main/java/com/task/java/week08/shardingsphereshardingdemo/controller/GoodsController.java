package com.task.java.week08.shardingsphereshardingdemo.controller;

import com.task.java.week08.shardingsphereshardingdemo.entity.Goods;
import com.task.java.week08.shardingsphereshardingdemo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author lgd
 * @Date 2020/12/8 上午10:23
 * @Version 1.0
 **/

@Controller
@RequestMapping(value = "/shardingsphere")
public class GoodsController {


	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	@ResponseBody
	public String demo(){
		return "hello world";
	}


	@RequestMapping(value = "/insertGoods", method = RequestMethod.GET)
	@ResponseBody
	public String insertGoods(Long id, Long buyerId){


		Goods insertableGoods = new Goods();
		insertableGoods.setId(id);
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
		Goods goods = goodsService.insert(insertableGoods);
		return goods.toString();
	}

	@RequestMapping(value = "/selectGoods", method = RequestMethod.GET)
	@ResponseBody
	public List<Goods> selectGoods(){

		List<Goods> goodsList =  goodsService.listAll();
		return goodsList;
	}

}
