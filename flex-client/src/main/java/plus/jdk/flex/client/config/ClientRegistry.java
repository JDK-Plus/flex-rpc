package plus.jdk.flex.client.config;

import lombok.Data;
import plus.jdk.flex.client.transport.IServiceInvoker;
import plus.jdk.flex.client.transport.VertxServiceInvoker;
import plus.jdk.flex.common.codec.IFlexDeserializer;
import plus.jdk.flex.common.codec.IFlexSerializer;
import plus.jdk.flex.common.codec.impl.ProtobufFlexDeserializer;
import plus.jdk.flex.common.codec.impl.ProtobufFlexSerializer;
import plus.jdk.flex.common.proxy.IDynamicProxy;
import plus.jdk.flex.common.proxy.JavassistDynamicProxy;

@Data
public class ClientRegistry {

    /**
     * 动态代理对象，用于生成和管理 RPC 客户端的代理类。
     */
    private IDynamicProxy dynamicProxy = new JavassistDynamicProxy();

    /**
     * RPC客户端的调用器实现类。
     */
    private IServiceInvoker serviceInvoker = new VertxServiceInvoker();

    /**
     * RPC客户端的序列化器，用于将对象序列化为字节流或从字节流反序列化为对象。
     * 默认使用ProtobufFlexSerializer进行序列化和反序列化。
     */
    private IFlexSerializer serializer = new ProtobufFlexSerializer();

    /**
     * RPC客户端的反序列化器，用于将字节流反序列化为对象。
     * 默认使用ProtobufFlexDeserializer进行反序列化。
     */
    private IFlexDeserializer deserializer = new ProtobufFlexDeserializer();
}
