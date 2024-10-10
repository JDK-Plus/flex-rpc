package plus.jdk.flex.common.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlexStatus {
    // 错误码
    success(0, "成功"),
    unknown_error(99, "未知错误"),
    read_time_out(100, "数据读取超时!"),
    class_not_found(101, "类型 %s 未找到"),
    service_not_found(102, "服务 %s 未找到服务端的实现"),
    response_is_null(103, "返回值为空"),

    cannot_find_available_instances(501, "无法找到远端的可用实例"),
    ;

    /**
     * 错误码的具体值。
     */
    private final Integer code;

    /**
     * 错误码对应的详细描述信息。
     */
    private final String message;

    /**
     * 根据给定的消息模板和参数列表，格式化字符串并返回。
     * @param args 参数列表，用于填充消息模板中的占位符。
     * @return 格式化后的字符串。
     */
    public String format(Object... args) {
        return String.format(message, args);
    }
}
