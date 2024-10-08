package plus.jdk.flex.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import plus.jdk.flex.server.global.ServerInterceptor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlexService {

    /**
     * 存储服务实例的成员变量。
     */
    private Object instance;

    /**
     * 存储服务拦截器的数组，用于在服务调用前后进行拦截处理。
     */
    private ServerInterceptor[] interceptors;
}
