package plus.jdk.flex.client.protocol.dns.impl;

import lombok.Data;
import plus.jdk.flex.client.protocol.dns.NameResolver;
import plus.jdk.flex.common.global.Constants;
import plus.jdk.flex.common.model.ServiceInstance;

import java.net.URI;
import java.util.List;

@Data
public class StaticNameResolver implements NameResolver {

    /**
     * 定义服务实例的方案名称。
     */
    private final String scheme = Constants.STATIC_SCHEME;


    @Override
    public List<ServiceInstance> resolve(URI name) {
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setAddress(name.getHost());
        serviceInstance.setPort(name.getPort());
        if(name.getPath() != null) {
            serviceInstance.setPath(name.getPath());
        }
        return List.of(serviceInstance);
    }
}
