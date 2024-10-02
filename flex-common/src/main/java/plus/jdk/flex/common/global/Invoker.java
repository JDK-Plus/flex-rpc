package plus.jdk.flex.common.global;

public interface Invoker {

    /**
     * 通过给定的代理对象和方法调用来执行方法。
     * @param proxy 要使用的代理对象。
     * @param method 要调用的方法。
     * @param args 调用方法时使用的参数。
     * @return 方法的返回值。
     * @throws Throwable 如果在方法调用过程中发生异常。
     */
    Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args);
}
