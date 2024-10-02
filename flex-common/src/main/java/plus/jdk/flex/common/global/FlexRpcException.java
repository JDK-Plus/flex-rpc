package plus.jdk.flex.common.global;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlexRpcException extends RuntimeException {

    public FlexRpcException(Exception e) {
        super(e);
    }
}
