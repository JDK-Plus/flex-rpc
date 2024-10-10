package plus.jdk.flex.client.global;

import plus.jdk.flex.client.config.ClientRegistry;
import plus.jdk.flex.client.protocol.ProtocolFacadeDelegate;
import plus.jdk.flex.client.transport.IServiceInvoker;
import plus.jdk.flex.common.codec.IFlexDeserializer;
import plus.jdk.flex.common.codec.IFlexSerializer;
import plus.jdk.flex.common.global.FlexRpcException;
import plus.jdk.flex.common.global.FlexStatus;
import plus.jdk.flex.common.global.SingletonProvider;
import plus.jdk.flex.common.model.RequestContext;
import plus.jdk.flex.common.model.ResponseContext;
import plus.jdk.flex.common.model.ServiceInstance;
import plus.jdk.flex.common.proxy.IDynamicProxy;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class FlexClient {

    /**
     * 客户端注册表，用于获取动态代理。
     */
    private final ClientRegistry clientRegistry;

    /**
     * 全局拦截器集合，用于在请求发起前执行拦截逻辑。
     */
    private final HashSet<ClientInterceptor> globalInterceptors = new HashSet<>();

    /**
     * 用于缓存消费者实例的并发哈希映射。
     */
    private final ConcurrentHashMap<Class<?>, Object> consumerCache = new ConcurrentHashMap<>();

    /**
     * 构造一个新的FlexClientFactory对象，用于管理客户端。
     * @param clientRegistry 客户端注册表，用于存储和检索客户端实例。
     */
    public FlexClient(ClientRegistry clientRegistry) {
        this.clientRegistry = clientRegistry;
    }

    /**
     * 构造一个新的FlexClientFactory对象。
     */
    public FlexClient() {
        this.clientRegistry = new ClientRegistry();
    }

    /**
     * 获取指定URI和类型的实例。
     */
    public synchronized <T> T acquireInstance(String uri, Class<T> injectionType) {
        URI serviceName = URI.create(uri);
        return acquireInstance(serviceName, injectionType, SingletonProvider.refer(FacadeContext.class));
    }


    /**
     * 获取指定类型的实例，并应用提供的拦截器。
     */
    public synchronized  <T> T acquireInstance(URI serviceName, Class<T> injectionType, FacadeContext facadeContext) {
        if(consumerCache.containsKey(injectionType)) {
            return (T) consumerCache.get(injectionType);
        }
        ProtocolFacadeDelegate protocolFacadeDelegate = SingletonProvider.refer(ProtocolFacadeDelegate.class);
        IDynamicProxy dynamicProxy = clientRegistry.getDynamicProxy();
        T instance = dynamicProxy.acquireProxy(injectionType, (proxy, method, args) -> {
            IFlexSerializer serializer = clientRegistry.getSerializer();
            IFlexDeserializer deserializer = clientRegistry.getDeserializer();
            Properties attributes = new Properties();
            RequestContext requestContext = new RequestContext();
            requestContext.setClazz(injectionType.getName());
            requestContext.setMethodName(method.getName());
            requestContext.setArgs(args);
            requestContext.setAttributes(attributes);
            ServiceInstance serviceInstance = protocolFacadeDelegate.resolveAddress(serviceName, requestContext, facadeContext.getLoadBalancer());
            requestContext.setServiceInstance(serviceInstance);
            List<ClientInterceptor> clientInterceptors = facadeContext.getInterceptors();
            clientInterceptors.addAll(globalInterceptors);
            for (ClientInterceptor interceptor : clientInterceptors) {
                interceptor.onRequestInitiation(requestContext);
            }
            IServiceInvoker serviceInvoker = clientRegistry.getServiceInvoker();
            byte[] respBinaryBytes = serviceInvoker.invokeMessage(requestContext, serializer.serialize(requestContext));
            ResponseContext responseContext = deserializer.deserialize(respBinaryBytes, ResponseContext.class);
            if(responseContext == null) {
                throw new FlexRpcException(FlexStatus.response_is_null);
            }
            if(responseContext.getStatus() != FlexStatus.success) {
                throw new FlexRpcException(responseContext.getMessage(), responseContext.getStatus());
            }
            for (ClientInterceptor interceptor : clientInterceptors) {
                interceptor.onRequestCompletion(requestContext, responseContext);
            }
            return responseContext.getResult();
        });
        consumerCache.put(injectionType, instance);
        return instance;
    }
}
