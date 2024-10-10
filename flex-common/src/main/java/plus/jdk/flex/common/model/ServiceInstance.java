package plus.jdk.flex.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInstance {

    /**
     * 服务实例的网络地址。例如：127.0.0.1
     */
    private String address;

    /**
     * 服务实例的端口号。
     */
    private Integer port;

    /**
     * 服务端提供的接收地址的路径信息。
     * 可覆盖全局配置
     */
    private String path = "/";

    /**
     * 服务实例的权重，用于负载均衡。
     * 权重越大，被分配到的流量越大
     */
    private Integer weight = 20;

    /**
     * 存储服务实例的自定义属性。
     * 这些属性可以用来扩展服务实例的信息，例如元数据等。
     */
    private Properties properties;

}
