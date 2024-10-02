package plus.jdk.flex.common.proxy;


import plus.jdk.flex.common.global.Invoker;

/**
 * 代理类实现定义
 */
public interface IDynamicProxy {

    /**
     * 获取指定接口的代理对象
     * @param interfaceClass 要代理的接口类
     * @param invoker 用于处理方法调用的 Invoker 对象
     * @return 代理对象
     */
    <T> T acquireProxy(Class<T> interfaceClass, Invoker invoker);
}
