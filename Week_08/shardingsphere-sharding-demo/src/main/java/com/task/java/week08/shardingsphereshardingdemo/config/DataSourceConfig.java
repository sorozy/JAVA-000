package com.task.java.week08.shardingsphereshardingdemo.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author lgd
 * @Date 2020/12/7 下午5:58
 * @Version 1.0
 **/

@Slf4j
@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource dataSource() {
		// 配置真实数据源
		Map<String, DataSource> dataSourceMap = new HashMap<>();

		// 配置第 1 个数据源
		DruidDataSource dataSource1 = new DruidDataSource();
		dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource1.setUrl("jdbc:mysql://127.0.0.1:3308/db0?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=UTC");
		dataSource1.setUsername("root");
		dataSource1.setPassword("12345Abc");
		dataSourceMap.put("ds0", dataSource1);

		// 配置第 2 个数据源
		DruidDataSource dataSource2 = new DruidDataSource();
		dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource2.setUrl("jdbc:mysql://127.0.0.1:3309/db1?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=UTC");
		dataSource2.setUsername("root");
		dataSource2.setPassword("12345Abc");
		dataSourceMap.put("ds1", dataSource2);

		// 配置 tb_goods 表规则
		ShardingTableRuleConfiguration orderTableRuleConfig = new ShardingTableRuleConfiguration("tb_goods", "ds${0..1}.tb_goods${0..15}");
		ShardingTableRuleConfiguration userOrderTableRuleConfig = new ShardingTableRuleConfiguration("tb_user", "ds0.tb_user${0..9}");

		// 配置分库策略
		orderTableRuleConfig.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("buyer_id", "dbShardingAlgorithm"));
		userOrderTableRuleConfig.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("id", "userDbShardingAlgorithm"));

		// 配置分表策略
		orderTableRuleConfig.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "tableShardingAlgorithm"));
		userOrderTableRuleConfig.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "userTableShardingAlgorithm"));

		// 配置分片规则
		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTables().add(orderTableRuleConfig);
		shardingRuleConfig.getTables().add(userOrderTableRuleConfig);

		// 配置分库算法
		Properties dbShardingAlgorithmrProps = new Properties();
		dbShardingAlgorithmrProps.setProperty("algorithm-expression", "ds${buyer_id % 2}");
		shardingRuleConfig.getShardingAlgorithms().put("dbShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("INLINE", dbShardingAlgorithmrProps));

		Properties userDbShardingAlgorithmrProps = new Properties();
		userDbShardingAlgorithmrProps.setProperty("algorithm-expression", "ds0");
		shardingRuleConfig.getShardingAlgorithms().put("userDbShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("INLINE", userDbShardingAlgorithmrProps));

		// 配置分表算法
		Properties tableShardingAlgorithmrProps = new Properties();
		tableShardingAlgorithmrProps.setProperty("algorithm-expression", "tb_goods${id % 16}");
		shardingRuleConfig.getShardingAlgorithms().put("tableShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("INLINE", tableShardingAlgorithmrProps));

		Properties userTableShardingAlgorithmrProps = new Properties();
		userTableShardingAlgorithmrProps.setProperty("algorithm-expression", "tb_user${id % 10}");
		shardingRuleConfig.getShardingAlgorithms().put("userTableShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("INLINE", userTableShardingAlgorithmrProps));


		DataSource dataSource = null;
		try {
			dataSource = ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(shardingRuleConfig), new Properties());
		} catch (SQLException e) {
			log.info("", e);
		}
		log.info("datasource : {}", dataSource);
		return dataSource;
	}
}
