学习笔记

# GC日志分析

命令 java -XX:+PrintGCDetails GCLogAnalysis

-Xloggc:gc.demo.log（导出日志）-XX:+PrintGCDateStamps（打印时间戳）

# 默认情况

```
[GC (Allocation Failure)
[PSYoungGen: 65536K->10742K(76288K)] 65536K->21628K(251392K), 0.0040638 secs]
[Times: user=0.03 sys=0.06, real=0.00 secs]
```

GC 表示年轻代GC

Allocation Failure 表示GC原因，分配空间失败（空间不足）

PSYoungGen: 65536K->10742K(76288K)  年轻代GC前后大小，括号内为年轻代大小

65536K->21628K(251392K)  整个堆GC前后大小，括号内为整个堆大小

user 用户线程时间

sys 系统线程时间

real 真正GC的时间

> 可以看到，第一次GC时，堆大小就是年轻代大小（65536K）。

```
[Full GC (Ergonomics)
[PSYoungGen: 10751K->0K(272896K)] 
[ParOldGen: 120961K->121417K(262144K)] 131712K->121417K(535040K), 
[Metaspace: 2637K->2637K(1056768K)], 0.0150500 secs] 
[Times: user=0.00 sys=0.00, real=0.02 secs]
```

Full GC

默认使用的是ParallelGC

# 串行GC

-XX:+UseSerialGC

```
[GC (Allocation Failure) 
[DefNew: 139776K->17472K(157248K), 0.0210434 secs] 
139776K->47171K(506816K), 0.0213852 secs] 
[Times: user=0.00 sys=0.02, real=0.02 secs]
```

DefNew 表示年轻代

> 设置-Xmx2048k以下都会提示
Error occurred during initialization of VM
GC triggered before VM initialization completed. Try increasing NewSize, current value 640K.
计算 640 * 3 = 1920 < 2048
堆空间需要大于2048K，2M

# 并行GC

 -XX:+UseParallelGC

> 设置-Xmx4096k以下都会提示
Error occurred during initialization of VM
GC triggered before VM initialization completed. Try increasing NewSize, current value 1536K.
计算 1536 * 3 = 4608 > 4096
堆空间需要大于 4096K，4M