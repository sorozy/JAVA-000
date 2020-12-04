## Week_07作业

### （必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。
- 只用存储过程简单测试了一下，测试10W数据的插入，一次如果插一条记录10W条要30多秒，之前测试过100W条要300多秒，改成一次插10条也就是values多值的情况就快很多了，插10W条只要3秒多
- 相关参数可以调整，一次插多少条可以根据一行数据的大小进行调整，参数是bulk_insert_buffer_size，还有放在一个事务里做插入的方式没有测试
- 用内存数据库H2结合preparedStatement测试也是一次多插几条数据会更快

```sql
# 3秒多
DROP PROCEDURE if exists insertdata;
delimiter //
CREATE PROCEDURE insertdata()
begin
declare yourid int;
set yourid = 1;
while yourid <= 100000 do
  insert into mydb.order(id, name)
  values(yourid, '张三'), (yourid+1, '张三'), (yourid+2, '张三'), (yourid+3, '张三'), (yourid+4, '张三'),
  (yourid+5, '张三'), (yourid+6, '张三'), (yourid+7, '张三'), (yourid+8, '张三'), (yourid+9, '张三');
  set yourid=yourid+10;
end while;
end//
delimiter ;
call insertdata();

# 30多秒
DROP PROCEDURE if exists insertdata;
delimiter //
CREATE PROCEDURE insertdata()
begin
declare yourid int;
set yourid = 1;
while yourid <= 100000 do
  insert into mydb.order(id, name)
  values(yourid, '张三');
  set yourid=yourid+1;
end while;
end//
delimiter ;
call insertdata();
```

### （必做）读写分离-动态切换数据源版本1.0
参考[https://www.cnblogs.com/yeya/p/11936239.html](https://www.cnblogs.com/yeya/p/11936239.html)实现

- 基于AbstractRoutingDataSource实现，比较重要的地方是要用ThreadLocal，然后是多数据源的配置，然后是优先级要比事务的优先级高
- aop也可以实现成根据方法名切换数据源
- 配置多个从库只需要再加数据源并加到map里就可以，负载均衡可以在DataSourceContextHolder的get方法里做
- 代码量不低，也确实有侵入性

### （必做）读写分离-数据库框架版本2.0
参考[https://blog.csdn.net/qq_31226223/article/details/110199271](https://blog.csdn.net/qq_31226223/article/details/110199271)

- 参考上面用Java config配置实现，只需要配置好就可以用，不过从日志上看不出更改操作走主库，读操作走从库，把主从复制关掉才可以看出来
- 配置文件实现参考官网和不少资料都没有配置成功，可能是更新大版本的原因
