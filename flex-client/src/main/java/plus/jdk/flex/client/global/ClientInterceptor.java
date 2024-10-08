package plus.jdk.flex.client.global;
import plus.jdk.flex.common.model.RequestContext;
import plus.jdk.flex.common.model.ResponseContext;

public interface ClientInterceptor {

    /**
     * 在请求初始化时执行的操作。
     */
    void onRequestInitiation(RequestContext request);

    /**
     * 在请求完成后被调用。
     */
    void onRequestCompletion(RequestContext request, ResponseContext response);
}
