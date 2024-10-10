package plus.jdk.flex.client.global;

import lombok.Data;
import plus.jdk.flex.client.protocol.ProtocolFacadeDelegate;
import plus.jdk.flex.client.protocol.balancer.ILoadBalancer;
import plus.jdk.flex.client.protocol.balancer.impl.RandomLoadBalancer;
import plus.jdk.flex.common.global.SingletonProvider;

import java.util.ArrayList;
import java.util.List;

@Data
public class FacadeContext {

    /**
     * 负责管理和提供负载均衡器实例的引用。
     */
    private ILoadBalancer loadBalancer = SingletonProvider.refer(RandomLoadBalancer.class);

    /**
     * 存储客户端拦截器的实例，用于在发送请求前进行拦截和处理。
     */
    private List<ClientInterceptor> interceptors = new ArrayList<>();

}
