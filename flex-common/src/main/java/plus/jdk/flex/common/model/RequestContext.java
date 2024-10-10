package plus.jdk.flex.common.model;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestContext {

    /**
     * RPC请求的URI信息。
     */
    private ServiceInstance serviceInstance;

    /**
     * 表示要调用的目标类。
     */
    private String clazz;

    /**
     * 目标方法的引用。
     */
    private String methodName;

    /**
     * 临时存储RPC请求的参数。
     */
    private Object[] args;

    /**
     * 存储RPC请求参数的类别信息。
     */
    private String[] argsClasses;

    /**
     * 存储RPC请求的附加属性信息。
     */
    private Properties attributes;

    /**
     * 获取连接超时时间（毫秒）。默认值为 -1 不使用该值，使用客户端全局配置的值。
     */
    private transient int connectTimeout = 5000;

    /**
     * 获取数据读取时间（毫秒）。默认值为 -1 不使用该值，使用客户端全局配置的值。
     */
    private transient int socketTimeout = 5000;

    /**
     * 获取传入参数的类别数组。
     */
    public Class<?>[] acquireClasses() {
        Class<?>[] classes = new Class<?>[args.length];
        if(ArrayUtil.isEmpty(args)) {
            return classes;
        }
        for (int i = 0; i < args.length; i++) {
            classes[i] = args[i].getClass();
        }
        return classes;
    }
}
