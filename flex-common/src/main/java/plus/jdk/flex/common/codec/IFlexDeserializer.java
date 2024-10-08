package plus.jdk.flex.common.codec;

public interface IFlexDeserializer {
    /**
     * 将字节数组反序列化为指定类的对象。
     * @param data 待反序列化的字节数组。
     * @return 反序列化后的对象。
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
