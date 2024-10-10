package plus.jdk.flex.client.protocol.balancer;


import plus.jdk.flex.client.protocol.dns.NameResolver;
import plus.jdk.flex.common.model.RequestContext;
import plus.jdk.flex.common.model.ServiceInstance;

import java.net.URI;
import java.util.List;

public interface ILoadBalancer {

    /**
     * 解析并返回一个SocketAddress对象。
     * @return 解析后的SocketAddress对象。
     */
    ServiceInstance resolveSocketAddress(URI serviceName, NameResolver nameResolver,  RequestContext requestContext);
}
