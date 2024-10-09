package plus.jdk.flex.server.global;

import lombok.extern.slf4j.Slf4j;
import plus.jdk.flex.common.codec.IFlexDeserializer;
import plus.jdk.flex.common.codec.IFlexSerializer;
import plus.jdk.flex.common.global.ClassUtils;
import plus.jdk.flex.common.global.FlexStatus;
import plus.jdk.flex.common.global.FlexRpcException;
import plus.jdk.flex.common.model.RequestContext;
import plus.jdk.flex.common.model.ResponseContext;
import plus.jdk.flex.server.listener.IFlexServer;
import plus.jdk.flex.server.config.ServerRegistry;
import plus.jdk.flex.server.listener.VertxFlexServer;
import plus.jdk.flex.server.model.FlexService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FlexDelegateServer {

    /**
     * 存储服务器属性的对象。
     */
    private final ServerRegistry serverRegistry;

    /**
     * 代表Flex服务器的接口实例，用于与Flex服务器交互。
     */
    private final IFlexServer flexServer;

    /**
     * 存储全局拦截器的集合。
     */
    private final HashSet<ServerInterceptor> globalInterceptors = new HashSet<>();

    /**
     * 存储已注册服务的映射关系，键为服务类，值为对应的FlexService对象。
     */
    private final ConcurrentHashMap<Class<?>, FlexService> flexServicesMap = new ConcurrentHashMap<>();

    /**
     * 构造一个新的 FlexDelegateServer 对象。
     * 初始化 serverRegistry 和 flexServer 成员变量。
     */
    public FlexDelegateServer() {
        this.serverRegistry = new ServerRegistry();
        this.flexServer = new VertxFlexServer();
    }

    /**
     * 构造一个FlexDelegateServer对象。
     * @param serverRegistry 服务器注册表对象，用于管理和维护服务器实例。
     * @param flexServer IFlexServer接口实现，提供与Flex服务器交互的方法。
     */
    public FlexDelegateServer(ServerRegistry serverRegistry, IFlexServer flexServer) {
        this.serverRegistry = serverRegistry;
        this.flexServer = flexServer;
    }

    /**
     * 启动 Flex 服务器。
     */
    public void startFlexServer() {
        flexServer.startServer(serverRegistry, this::dispatchRequestByContext);
    }

    /**
     * 注册全局拦截器。
     * @param interceptors 要注册的拦截器。
     */
    public void registerGlobalInterceptors(ServerInterceptor... interceptors) {
        globalInterceptors.addAll(List.of(interceptors));
    }

    /**
     * 注册服务并添加拦截器。
     * @param service 要注册的服务对象。
     * @param interceptors 可变长度参数，用于指定要添加到服务的拦截器。
     */
    public void registerService(Object service, ServerInterceptor... interceptors) {
        FlexService flexService = new FlexService(service, interceptors);
        for (Class<?> clazz : ClassUtils.getAllImplementedInterfaces(service.getClass())) {
            if(flexServicesMap.containsKey(clazz)) {
                log.error("There are multiple implementations of the interface [{}].", clazz.getName());
            }
            flexServicesMap.put(clazz, flexService);
        }
    }

    /**
     * 根据上下文派发请求。
     * @param dataBytes 请求数据的字节流表示。
     * @return 处理后的响应数据的字节流表示。
     */
    protected byte[] dispatchRequestByContext(byte[] dataBytes) {
        IFlexDeserializer deserializer = serverRegistry.getDeserializer();
        IFlexSerializer serializer = serverRegistry.getSerializer();
        RequestContext requestContext = deserializer.deserialize(dataBytes, RequestContext.class);
        Class<?> clazz = null;
        try {
            clazz = Class.forName(requestContext.getClazz());
        } catch (Exception e) {
            throw new FlexRpcException(FlexStatus.class_not_found, requestContext.getClazz());
        }
        Method method = ClassUtils.getDeclaredMethod(clazz, requestContext.getMethodName(), requestContext.acquireClasses());
        FlexService flexService = flexServicesMap.get(clazz);
        if(flexService == null) {
            throw new FlexRpcException(FlexStatus.class_not_found, requestContext.getClazz());
        }
        ResponseContext responseContext = new ResponseContext();
        responseContext.setAttributes(requestContext.getAttributes());
        Collection<ServerInterceptor> serverInterceptors = new ArrayList<>(this.globalInterceptors);
        serverInterceptors.addAll(List.of(flexService.getInterceptors()));
        for (ServerInterceptor serverInterceptor : serverInterceptors) {
            serverInterceptor.onRequestInitiation(requestContext, responseContext.getAttributes());
        }
        Object result;
        try {
            result = method.invoke(flexService.getInstance(), requestContext.getArgs());
            responseContext.setResult(result);
        } catch (Exception e) {
            if (e instanceof FlexRpcException) {
                responseContext.setStatus(((FlexRpcException) e).getFlexStatus());
                throw (FlexRpcException) e;
            }
            responseContext.setMessage(e.getMessage());
            responseContext.setStatus(FlexStatus.unknown_error);
        } finally {
            for (ServerInterceptor serverInterceptor : serverInterceptors) {
                serverInterceptor.onRequestCompletion(requestContext, responseContext);
            }
        }
        return serializer.serialize(responseContext);
    }
}
