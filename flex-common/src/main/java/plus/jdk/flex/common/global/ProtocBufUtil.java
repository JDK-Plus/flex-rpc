package plus.jdk.flex.common.global;

import cn.hutool.core.codec.Base64;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.val;

import java.nio.charset.StandardCharsets;

/**
 * 使用protobuf来序列化对象
 */
public class ProtocBufUtil {


    /**
     * 将给定的对象序列化为 UTF-8 编码的字符串。
     * @param obj 要序列化的对象。
     * @return 序列化后的字符串。
     */
    public static <T> String serializeToString(T obj) {
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return Base64.encode(ProtostuffIOUtil.toByteArray(obj, schema, buffer));
        } finally {
            buffer.clear();
        }
    }


    /**
     * 从给定的字符串反序列化对象。
     * @param data 要反序列化的字符串数据。
     * @param clazz 目标对象的类。
     * @return 反序列化后的对象。
     */
    public static <T> T deserializeFromString(String data, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(Base64.decode(data), obj, schema);
        return obj;
    }

    public static <T> byte[] serialize(T object) {
        ObjectWrapper<T> wrapper = new ObjectWrapper<>(object);
        val schema = RuntimeSchema.getSchema(ObjectWrapper.class);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return ProtostuffIOUtil.toByteArray(wrapper, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        ObjectWrapper<T> wrapper = new ObjectWrapper<T>();
        val schema = RuntimeSchema.getSchema(ObjectWrapper.class);
        ProtostuffIOUtil.mergeFrom(data, wrapper, schema);
        return (T) wrapper.getData();
    }

}
