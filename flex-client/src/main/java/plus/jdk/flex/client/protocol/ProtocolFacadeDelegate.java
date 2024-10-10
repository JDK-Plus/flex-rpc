package plus.jdk.flex.client.protocol;


import plus.jdk.flex.client.config.ClientRegistry;
import plus.jdk.flex.client.protocol.balancer.ILoadBalancer;
import plus.jdk.flex.client.protocol.balancer.impl.RandomLoadBalancer;
import plus.jdk.flex.client.protocol.dns.NameResolver;
import plus.jdk.flex.common.model.RequestContext;
import plus.jdk.flex.common.model.ServiceInstance;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ProtocolFacadeDelegate {

    /**
     * 客户端注册表，用于管理和维护客户端的注册信息。
     */
    private ClientRegistry clientRegistry;

    /**
     * 解析指定服务的地址并返回对应的 ServiceInstance。
     */
    public ServiceInstance resolveAddress(URI serviceName, RequestContext requestContext, ILoadBalancer loadBalancer) {
        NameResolver nameResolver = NameResolverFacadeProvider.achquireNameResolver(serviceName);
        return loadBalancer.resolveSocketAddress(serviceName, nameResolver, requestContext);
    }
}
