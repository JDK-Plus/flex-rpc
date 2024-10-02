package plus.jdk.flex.common.proxy;

import org.junit.Test;
import plus.jdk.flex.common.global.GreetingService;
import plus.jdk.flex.common.global.Invoker;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class JdkDynamicProxyTest {

    @Test
    public void acquireProxy() {
        JdkDynamicProxy dynamicProxy = new JdkDynamicProxy();
        GreetingService greetingService = dynamicProxy.acquireProxy(GreetingService.class, new Invoker() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                return String.format("Hello %s!", args);
            }
        });
        assertNotNull(greetingService);
        assertEquals("Hello tom!", greetingService.hello("tom"));
    }
}