package plus.jdk.flex.common.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 包装类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectWrapper<T> {
    private T data;
}
