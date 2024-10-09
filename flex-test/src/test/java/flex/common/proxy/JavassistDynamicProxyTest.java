package flex.common.proxy;

import flex.common.global.GreetingService;
import org.junit.Test;
import plus.jdk.flex.common.global.Invoker;
import plus.jdk.flex.common.proxy.JavassistDynamicProxy;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavassistDynamicProxyTest {

    @Test
    public void acquireProxyTest() {
        JavassistDynamicProxy dynamicProxy = new JavassistDynamicProxy();
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