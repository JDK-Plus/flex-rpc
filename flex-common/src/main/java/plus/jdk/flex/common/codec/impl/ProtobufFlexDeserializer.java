package plus.jdk.flex.common.codec.impl;

import plus.jdk.flex.common.codec.IFlexDeserializer;
import plus.jdk.flex.common.global.ProtocBufUtil;

public class ProtobufFlexDeserializer implements IFlexDeserializer {

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return ProtocBufUtil.deserialize(data, clazz);
    }
}
