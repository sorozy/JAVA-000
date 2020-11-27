
-- 用户：
-- demo_user 主键、账号、密码、昵称、姓名、状态、注册时间、最后登录时间
CREATE TABLE demo_user(
user_id INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '用户ID',
account VARCHAR(32) NOT NULL COMMENT '账号',
password VARCHAR(32) NOT NULL COMMENT '密码',
nickname VARCHAR(64) NOT NULL COMMENT '昵称',
realname VARCHAR(64) COMMENT '姓名',
status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0禁用，1正常可用',
register_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
last_login_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
PRIMARY KEY pk_user_id(user_id)
) ENGINE = innodb COMMENT '用户信息表';


-- 商品：
-- demo_product 主键、产品名称、产品类别、产品价格、产品状态、产品创建时间、修改时间
CREATE TABLE demo_product(
product_id INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '商品ID',
product_name VARCHAR(32) NOT NULL COMMENT '产品名称',
product_category VARCHAR(32) NOT NULL COMMENT '产品类别',
price DECIMAL(8,2) NOT NULL COMMENT '产品价格',
status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0草稿，1已上架，2已上架',
create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '产品创建时间',
last_update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
PRIMARY KEY pk_product_id(product_id)
) ENGINE = innodb COMMENT '商品信息表';


--订单：
--demo_order 主键、产品id、产品数量、订单总价、订单状态、订单创建时间、订单最后更新时间
CREATE TABLE demo_order(
order_id INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '订单ID',
product_id INT NOT NULL COMMENT '产品id',
product_num INT NOT NULL COMMENT '产品数量',
total_price DECIMAL(8,2) NOT NULL COMMENT '订单总价格',
status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0未付款，2已付款',
create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '产品创建时间',
last_update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单最后更新时间',
PRIMARY KEY pk_order_id(order_id)
) ENGINE = innodb COMMENT '订单信息表';


--订单明细表
--订单明细主键、订单主键、