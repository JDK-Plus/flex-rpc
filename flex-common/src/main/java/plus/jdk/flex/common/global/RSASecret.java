package plus.jdk.flex.common.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义描述 RSA 的密钥
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSASecret {
    /**
     * 业务号
     */
    private String appKey;

    /**
     * RSA公钥,
     * 发送请求时客户端使用公钥加密，服务端使用私钥解密
     * 处理完成返回数据时服务端使用私钥加密，客户端使用公钥解密
     */
    private String publicKey;

    /**
     * RSA 私钥，
     * 发送请求时客户端使用公钥加密，服务端使用私钥解密
     * 处理完成返回数据时服务端使用私钥加密，客户端使用公钥解密
     */
    private String privateKey;

    /**
     * 是否启用该 RSA 密钥
     */
    private Boolean enable = false;

    /**
     * 服务端配置是否允许当前客户端调用所有接口
     */
    private Boolean allowAll = true;

    /**
     * 服务白名单，用于指定允许调用的接口类。若allowAll在这个实现里面，则不使用这个字段来做判定
     */
    private List<Class<?>> serviceWhiteList = new ArrayList<>();
}
