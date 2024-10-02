package plus.jdk.flex.common.proxy;

import plus.jdk.flex.common.global.ClassLoaderUtils;
import plus.jdk.flex.common.global.Invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK 动态代理类实现
 */
public class JdkDynamicProxy implements IDynamicProxy {
    @Override
    public <T> T acquireProxy(Class<T> interfaceClass, Invoker invoker) {
        ClassLoader classLoader = ClassLoaderUtils.getCurrentClassLoader();
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return invoker.invoke(proxy, method, args);
            }
        };
        @SuppressWarnings("unchecked")
        T result = (T) Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, invocationHandler);
        return result;
    }
}
