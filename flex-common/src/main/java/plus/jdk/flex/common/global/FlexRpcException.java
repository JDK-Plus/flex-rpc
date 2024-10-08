package plus.jdk.flex.common.global;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlexRpcException extends RuntimeException {

    /**
     * 异常状态码。
     */
    private FlexStatus flexStatus;

    /**
     * 构造一个新的FlexRpcException对象，使用指定的异常作为原因。
     * @param e 原始异常对象
     */
    public FlexRpcException(Exception e) {
        super(e);
        this.flexStatus = FlexStatus.unknown_error;
    }

    /**
     * 构造一个FlexRpcException对象，带有指定的错误码和引发异常。
     */
    public FlexRpcException(FlexStatus flexStatus, Exception e, Object... args) {
        super(flexStatus.format(args), e);
        this.flexStatus = flexStatus;
    }

    /**
     * 构造一个 FlexRpcException 对象，带有指定的错误码和可变数量的参数。
     */
    public FlexRpcException(FlexStatus flexStatus, Object... args) {
        super(flexStatus.format(args));
        this.flexStatus = flexStatus;
    }

    /**
     * 构造一个新的FlexRpcException对象。
     */
    public FlexRpcException(String message, FlexStatus flexStatus) {
        super(message);
        this.flexStatus = flexStatus;
    }
}
