package plus.jdk.flex.common.proxy;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import plus.jdk.flex.common.global.Invoker;

/**
 * 基于 javassist 实现的动态代理
 */
public class JavassistDynamicProxy implements IDynamicProxy {

    @Override
    public <T> T acquireProxy(Class<T> interfaceClass, Invoker invoker) {
        ProxyFactory factory = new ProxyFactory();
        factory.setInterfaces(new Class[]{interfaceClass});
        MethodHandler methodHandler = (self, thisMethod, proceed, args) -> {
            return invoker.invoke(self, thisMethod, args);
        };
        try{
            @SuppressWarnings("unchecked")
            T proxy = (T) factory.create(new Class[0], new Object[0], methodHandler);
            return proxy;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
