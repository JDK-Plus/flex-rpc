package plus.jdk.flex.client.protocol.dns;

import plus.jdk.flex.common.model.ServiceInstance;

import java.net.URI;
import java.util.List;


public interface NameResolver {

    /**
     * 获取URL的协议部分。
     *
     * @return URL的协议部分，如http或https。
     */
    String getScheme();

    /**
     * 解析指定名称的地址，返回对应的 SocketAddress 列表。
     * @param name 要解析的地址名称。
     * @return 解析后的 SocketAddress 列表。
     */
    List<ServiceInstance> resolve(URI name);
}
