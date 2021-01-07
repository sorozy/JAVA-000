学习笔记
### Homework
1. （必做）配置 redis 的主从复制，sentinel 高可用，Cluster 集群。
```shell
# 启动 redis，replica命令在config中，以6376为master
redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/redis6376.conf

redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/redis6377.conf

redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/redis6378.conf
```
```shell

#验证主从
redis-cli -p 6376      
       
127.0.0.1:6376> info Replication
# Replication
role:master
connected_slaves:2
slave0:ip=127.0.0.1,port=6377,state=online,offset=60741,lag=0
slave1:ip=127.0.0.1,port=6378,state=online,offset=60741,lag=0
master_replid:7d0ecb17b62c266085a1251d19055016407e248c
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:60741
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:60741
```

```shell
# 哨兵启动
redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/sentinel26376.conf --sentinel

redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/sentinel26377.conf --sentinel

redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/sentinel26377.conf --sentinel

# 哨兵集群验证
% redis-cli -p 26376
127.0.0.1:26376> info Sentinel
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=127.0.0.1:6376,slaves=2,sentinels=3
```

- cluster
```shell
sed 's/6376/6375/g' redis6376.conf >> redis6375.conf
sed 's/6376/6374/g' redis6376.conf >> redis6374.conf
sed 's/6376/6373/g' redis6376.conf >> redis6373.conf
# 并修改启动cluster配置

# 启动节点 6375 6374  6373用以组建最小的cluster集群 
redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/redis6375.conf

redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/redis6374.conf

redis-server /Users/sorozy/idea_ws/git/JAVA-000/Week_12/config/redis6373.conf

redis-cli --cluster create 127.0.0.1:6373 127.0.0.1:6374 127.0.0.1:6375
# 结果
>>> Performing hash slots allocation on 3 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
M: 9e82524a7edc6d6dd8c4e01eb4bfb7bb63921627 127.0.0.1:6373
   slots:[0-5460] (5461 slots) master
M: 02ab862af87f8c1017fa82a870554e74ebd23de0 127.0.0.1:6374
   slots:[5461-10922] (5462 slots) master
M: 6c4b5a537deb96de7e6b328be2f51755a57a91fd 127.0.0.1:6375
   slots:[10923-16383] (5461 slots) master
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join

>>> Performing Cluster Check (using node 127.0.0.1:6373)
M: 9e82524a7edc6d6dd8c4e01eb4bfb7bb63921627 127.0.0.1:6373
   slots:[0-5460] (5461 slots) master
M: 02ab862af87f8c1017fa82a870554e74ebd23de0 127.0.0.1:6374
   slots:[5461-10922] (5462 slots) master
M: 6c4b5a537deb96de7e6b328be2f51755a57a91fd 127.0.0.1:6375
   slots:[10923-16383] (5461 slots) master
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```
集群验证
```text
% redis-cli -p 6373                                                    
127.0.0.1:6373> cluster info
cluster_state:ok
cluster_slots_assigned:16384
cluster_slots_ok:16384
cluster_slots_pfail:0
cluster_slots_fail:0
cluster_known_nodes:3
cluster_size:3
cluster_current_epoch:3
cluster_my_epoch:1
cluster_stats_messages_ping_sent:64
cluster_stats_messages_pong_sent:62
cluster_stats_messages_sent:126
cluster_stats_messages_ping_received:60
cluster_stats_messages_pong_received:64
cluster_stats_messages_meet_received:2
cluster_stats_messages_received:126
```



---

2. （选做）练习示例代码里下列类中的作业题:
08cache/redis/src/main/java/io/kimmking/cache/RedisApplication.java

3. （选做☆）练习 redission 的各种功能。

4. （选做☆☆）练习 hazelcast 的各种功能。

5. （选做☆☆☆）搭建 hazelcast 3 节点集群，写入 100 万数据到一个 map，模拟和演 示高可用。