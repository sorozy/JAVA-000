package com.study;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 */
public class Homework03 {

    public static void main(String[] args) throws Exception {

        long start=System.currentTimeMillis();
        //方法一
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> result = executor.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return sum();
            }
        });
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

//        int result = sum(); //这是得到的返回值
        executor.shutdown();
        // 确保  拿到result 并输出
        try {
            System.out.println("异步计算结果为："+result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程

        //方法二
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        new Thread(task).start();
        try {
            System.out.println("result: " + task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //方法三
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = sum();
                System.out.println("result: " + result);
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await();
        System.out.println("方法三==>主线程执行结束。。。。");

        //方法四
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1, new Runnable() {
            @Override
            public void run() {
                System.out.println("方法四==>主线程执行结束。。。。");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = sum();
                System.out.println("result: " + result);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //方法五
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = sum();
                System.out.println("方法五-result: " + result);
            }
        });
        t1.start();
        t1.join();
        System.out.println("方法五==>主线程执行结束。。。。");



    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
