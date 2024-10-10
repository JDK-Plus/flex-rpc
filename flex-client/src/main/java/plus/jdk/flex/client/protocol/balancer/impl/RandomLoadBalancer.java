package plus.jdk.flex.client.protocol.balancer.impl;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import plus.jdk.flex.client.protocol.NameResolverFacadeProvider;
import plus.jdk.flex.client.protocol.balancer.ILoadBalancer;
import plus.jdk.flex.client.protocol.dns.NameResolver;
import plus.jdk.flex.common.global.FlexRpcException;
import plus.jdk.flex.common.global.FlexStatus;
import plus.jdk.flex.common.model.RequestContext;
import plus.jdk.flex.common.model.ServiceInstance;

import java.net.URI;
import java.util.List;
import java.util.Random;

@Slf4j
public class RandomLoadBalancer implements ILoadBalancer {
    /**
     * 解析服务地址
     * @param serviceFacade 服务外观对象
     * @return 解析后的套接字地址
     */
    @Override
    public ServiceInstance resolveSocketAddress(URI serviceName, NameResolver nameResolver, RequestContext requestContext) {
        List<ServiceInstance> serviceInstances = nameResolver.resolve(serviceName);
        if(CollectionUtil.isEmpty(serviceInstances)) {
            log.error("cannot_find_available_instances, :{}", serviceInstances);
            throw new FlexRpcException(FlexStatus.cannot_find_available_instances, serviceName.toString());
        }
        if(serviceInstances.size() == 1) {
            return serviceInstances.getFirst();
        }
        Random random = new Random();
        int randomIndex = random.nextInt(serviceInstances.size() - 1);
        return serviceInstances.get(randomIndex > 0 ? serviceInstances.size() - 1 : randomIndex);
    }
}
