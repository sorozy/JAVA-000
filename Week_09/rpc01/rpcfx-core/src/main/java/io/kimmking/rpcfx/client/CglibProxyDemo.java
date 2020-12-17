package io.kimmking.rpcfx.client;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

public class CglibProxyDemo {

    public static void main(String[] args) {
        Demo res = (Demo) Enhancer.create(Demo.class, null, new CglibProxyInterceptor());
        System.out.println(res.get1());
    }

    //cglib代理通过继承方式,运行期生成代理类
    //判断代理类中是否实现MethodInterceptor接口,如果实现会执行MethodInterceptor#intercept()
    //MethodInterceptor#intercept(),可以执行被代理类对象方法,可以对执行被代理类对象方法进行增强
    public static class CglibProxyInterceptor implements MethodInterceptor{
        @Override
        public Object intercept(Object o, Method method, Object[] params, MethodProxy proxy) throws Throwable {
            if (method.isAnnotationPresent(CglibProxy.class)) {
                System.out.println("========前置操作=========");
                //执行被代理类对象方法(父类方法)
                Object res = proxy.invokeSuper(o, params);
                System.out.println("执行被代理类方法结果====" + res);
                System.out.println("========后置操作=========");
                return res;
            }
            return proxy.invokeSuper(o, params);
        }
    }

    public static class Demo{
        public String get(){
            return "menglingchao";
        }
        @CglibProxy
        public String get1(){
            return "menglingchao1111";
        }
    }
}
