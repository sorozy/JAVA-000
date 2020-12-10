# 学习笔记

> 本周完成了必做题：
> 
> 1. 设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。
并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github
> 2. 基于hmily TCC或ShardingSphere的Atomikos XA实现一个简单的分布式 事务应用demo（二选一），提交到github

---

### **第一题**

#### 1.1 创建数据库/表

```
分别在两个数据库实例中创建数据库db0/db1和表tb_goods_0 - tb_goods_15

# 创建数据库db0及其分表
create database tb0;

use db0;

CREATE TABLE `tb_goods0` (
  `id` bigint(20) NOT NULL COMMENT '商品id',
  `goods_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `seller_id` bigint(20) NOT NULL COMMENT '卖家id',
  `first_category` int(11) DEFAULT NULL COMMENT '第一级分类',
  `second_category` int(11) DEFAULT NULL COMMENT '第二级分类',
  `weight` decimal(10,3) NOT NULL COMMENT '商品重量',
  `unit` tinyint(2) NOT NULL COMMENT '商品单位',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `seller` (`seller_id`),
  KEY `goods_name` (`goods_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电商场景-商品表';

...
...

CREATE TABLE `tb_goods15` (
  `id` bigint(20) NOT NULL COMMENT '商品id',
  `goods_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `seller_id` bigint(20) NOT NULL COMMENT '卖家id',
  `first_category` int(11) DEFAULT NULL COMMENT '第一级分类',
  `second_category` int(11) DEFAULT NULL COMMENT '第二级分类',
  `weight` decimal(10,3) NOT NULL COMMENT '商品重量',
  `unit` tinyint(2) NOT NULL COMMENT '商品单位',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `seller` (`seller_id`),
  KEY `goods_name` (`goods_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电商场景-商品表';




# 创建数据库db1及其分表
create database tb1;

use db1;

CREATE TABLE `tb_goods0` (
  `id` bigint(20) NOT NULL COMMENT '商品id',
  `goods_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `seller_id` bigint(20) NOT NULL COMMENT '卖家id',
  `first_category` int(11) DEFAULT NULL COMMENT '第一级分类',
  `second_category` int(11) DEFAULT NULL COMMENT '第二级分类',
  `weight` decimal(10,3) NOT NULL COMMENT '商品重量',
  `unit` tinyint(2) NOT NULL COMMENT '商品单位',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `seller` (`seller_id`),
  KEY `goods_name` (`goods_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电商场景-商品表';

...
...

CREATE TABLE `tb_goods15` (
  `id` bigint(20) NOT NULL COMMENT '商品id',
  `goods_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `seller_id` bigint(20) NOT NULL COMMENT '卖家id',
  `first_category` int(11) DEFAULT NULL COMMENT '第一级分类',
  `second_category` int(11) DEFAULT NULL COMMENT '第二级分类',
  `weight` decimal(10,3) NOT NULL COMMENT '商品重量',
  `unit` tinyint(2) NOT NULL COMMENT '商品单位',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `updated_at` bigint(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `seller` (`seller_id`),
  KEY `goods_name` (`goods_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='电商场景-商品表';
```

#### 1.2 新建springboot项目shardingsphere-sharding-demo

1.2.1 配置pom文件
```
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>2.1.4</version>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.apache.shardingsphere</groupId>
			<artifactId>shardingsphere-jdbc-core</artifactId>
			<version>5.0.0-alpha</version>
		</dependency>

		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid-spring-boot-starter</artifactId>
			<version>1.1.24</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
		</dependency>
	</dependencies>
```
1.2.2 配置DataSourceConfig文件

```
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

		// 配置 t_order 表规则
		ShardingTableRuleConfiguration orderTableRuleConfig = new ShardingTableRuleConfiguration("tb_goods", "ds${0..1}.tb_goods${0..15}");

		// 配置分库策略
		orderTableRuleConfig.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("buyer_id", "dbShardingAlgorithm"));

		// 配置分表策略
		orderTableRuleConfig.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "tableShardingAlgorithm"));

		// 省略配置 t_order_item 表规则...
		// ...

		// 配置分片规则
		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTables().add(orderTableRuleConfig);

		// 配置分库算法
		Properties dbShardingAlgorithmrProps = new Properties();
		dbShardingAlgorithmrProps.setProperty("algorithm-expression", "ds${buyer_id % 2}");
		shardingRuleConfig.getShardingAlgorithms().put("dbShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("INLINE", dbShardingAlgorithmrProps));

		// 配置分表算法
		Properties tableShardingAlgorithmrProps = new Properties();
		tableShardingAlgorithmrProps.setProperty("algorithm-expression", "tb_goods${id % 16}");
		shardingRuleConfig.getShardingAlgorithms().put("tableShardingAlgorithm", new ShardingSphereAlgorithmConfiguration("INLINE", tableShardingAlgorithmrProps));

		DataSource dataSource = null;
		try {
			dataSource = ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(shardingRuleConfig), new Properties());

		} catch (SQLException e) {
			log.info("", e);
		}
		log.info("datasource : {}", dataSource);
		return dataSource;
	}
```

1.2.3 配置Service和Controller


```
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
}


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
```


### **第二题： 使用hmily完成柔性分布式事物**

#### 2.1 引入jar包

```
<dependency>
			<groupId>org.dromara</groupId>
			<artifactId>hmily-spring-boot-starter-dubbo</artifactId>
			<version>2.1.1</version>
		</dependency>
```
#### 2.2 配置hmily.yml

```
hmily:
  server:
    configMode: local
    appName: hmily-demo
  #  如果server.configMode eq local 的时候才会读取到这里的配置信息.
  config:
    appName: hmily-demo
    serializer: kryo
    contextTransmittalMode: threadLocal
    scheduledThreadMax: 16
    scheduledRecoveryDelay: 60
    scheduledCleanDelay: 60
    scheduledPhyDeletedDelay: 600
    scheduledInitDelay: 30
    recoverDelayTime: 60
    cleanDelayTime: 180
    limit: 200
    retryMax: 10
    bufferSize: 8192
    consumerThreads: 16
    asyncRepository: true
    autoSql: true
    phyDeleted: true
    storeDays: 3
    repository: mysql

repository:
  database:
    driverClassName: com.mysql.jdbc.Driver
    url : jdbc:mysql://127.0.0.1:3308/hmily?useUnicode=true&characterEncoding=utf8
    username: root
    password: 12345Abc
    maxActive: 20
    minIdle: 10
    connectionTimeout: 30000
    idleTimeout: 600000
    maxLifetime: 1800000

metrics:
  metricsName: prometheus
  host:
  port: 9091
  async: true
  threadCount : 16
  jmxConfig:

```
#### 2.3 在事物实现类通过注解实现分布式事物

```
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
```

#### 2.3 实现控制器调用两种情况的实现

```
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
```

