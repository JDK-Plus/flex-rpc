package plus.jdk.flex.client.transport;

import plus.jdk.flex.client.config.ClientRegistry;
import plus.jdk.flex.common.model.RequestContext;

public interface IServiceInvoker {

    /**
     * 发送消息给指定的接收者。
     * @param context RequestContext 对象，包含发送消息所需的上下文信息。
     * @param body 消息体，作为字节数组形式传递。
     * @return 发送结果，返回一个字节数组。
     */
    byte[] invokeMessage(RequestContext context, byte[] body);
}
