package plus.jdk.flex.common.codec;

public interface IFlexSerializer {
    /**
     * 将对象序列化为字节数组。
     * @param obj 要序列化的对象。
     * @return 对象的序列化字节数组。
     */
    byte[] serialize(Object obj);
}
