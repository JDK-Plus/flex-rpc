package plus.jdk.flex.client.protocol;


import plus.jdk.flex.common.model.ServiceInstance;

import java.net.SocketAddress;
import java.net.URI;
import java.util.List;

public interface INameResolverConfigurer {

    /**
     * 刷新对应的uri对应的实例列表, 默认每10秒执行一次
     */
    List<ServiceInstance> configurationName(URI targetUri);
}
