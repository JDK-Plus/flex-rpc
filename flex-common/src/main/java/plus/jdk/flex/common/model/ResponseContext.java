package plus.jdk.flex.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import plus.jdk.flex.common.global.FlexStatus;

import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseContext {

    /**
     * 储存 Fusion RPC 响应的结果对象。
     */
    private Object result;

    /**
     * 储存 Flex RPC 响应的错误码，初始化为成功状态。
     */
    private FlexStatus status = FlexStatus.success;

    /**
     * 储存 RPC 响应的错误或提示信息。
     */
    private String message = FlexStatus.success.getMessage();

    /**
     * 存储 Fusion RPC 响应的附加属性。
     */
    private Properties attributes;
}
