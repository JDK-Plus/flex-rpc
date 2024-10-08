package plus.jdk.flex.common.codec.impl;

import plus.jdk.flex.common.codec.IFlexSerializer;
import plus.jdk.flex.common.global.ProtocBufUtil;

public class ProtobufFlexSerializer implements IFlexSerializer {

    @Override
    public byte[] serialize(Object obj) {
        return ProtocBufUtil.serialize(obj);
    }
}
