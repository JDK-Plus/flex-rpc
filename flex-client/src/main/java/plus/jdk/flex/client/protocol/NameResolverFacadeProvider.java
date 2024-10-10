package plus.jdk.flex.client.protocol;


import plus.jdk.flex.client.config.ClientRegistry;
import plus.jdk.flex.client.protocol.dns.NameResolver;
import plus.jdk.flex.client.protocol.dns.impl.StaticNameResolver;
import plus.jdk.flex.common.global.Constants;
import plus.jdk.flex.common.global.SingletonProvider;
import plus.jdk.flex.common.model.RequestContext;
import plus.jdk.flex.common.model.ServiceInstance;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class NameResolverFacadeProvider {

    /**
     * 存储不同服务的名称解析器实例的映射表。
     * 键为服务名称，值为对应的 NameResolver 实例。
     */
    private static final ConcurrentHashMap<String, NameResolver> SERVICE_INSTANCE_MAP = new ConcurrentHashMap<>();

    static {
        registerNameResolver(SingletonProvider.refer(StaticNameResolver.class));
    }

    /**
     * 根据给定的 URI 获取对应的名称解析器。
     */
    public static NameResolver achquireNameResolver(URI uri) {
        return SERVICE_INSTANCE_MAP.get(uri.getScheme());
    }

    /**
     * 注册一个名为解析器，用于处理指定方案的请求。
     * @param scheme 需要解析的URL方案，例如“http”或“https”。
     * @param nameResolver 实现了NameResolver接口的对象，用于解析指定方案的URL。
     */
    public static void registerNameResolver(NameResolver nameResolver) {
        SERVICE_INSTANCE_MAP.put(nameResolver.getScheme(), nameResolver);
    }
}
