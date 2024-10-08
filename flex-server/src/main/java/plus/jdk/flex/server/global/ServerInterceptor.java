package plus.jdk.flex.server.global;

import plus.jdk.flex.common.model.RequestContext;
import plus.jdk.flex.common.model.ResponseContext;

import java.util.Properties;

/**
 * 服务端接收请求的拦截器
 */
public interface ServerInterceptor {
    /**
     * 在请求初始化时执行的操作。
     */
    void onRequestInitiation(RequestContext request, Properties attributes);

    /**
     * 在请求完成后被调用。
     */
    void onRequestCompletion(RequestContext request, ResponseContext response);
}
