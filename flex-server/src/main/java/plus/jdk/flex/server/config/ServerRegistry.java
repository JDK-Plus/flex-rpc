package plus.jdk.flex.server.config;

import lombok.Data;
import plus.jdk.flex.common.codec.IFlexDeserializer;
import plus.jdk.flex.common.codec.IFlexSerializer;
import plus.jdk.flex.common.codec.impl.ProtobufFlexDeserializer;
import plus.jdk.flex.common.codec.impl.ProtobufFlexSerializer;

@Data
public class ServerRegistry {

    /**
     * 服务器监听的端口号。
     */
    private int port = 10200;

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
