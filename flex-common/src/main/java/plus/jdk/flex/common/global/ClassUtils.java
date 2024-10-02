package plus.jdk.flex.common.global;

import cn.hutool.core.util.ClassUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassUtils extends ClassUtil {
    public static Set<Class<?>> getAllImplementedInterfaces(Class<?> clazz) {
        Set<Class<?>> interfaces = new HashSet<>();
        while (clazz != null) {
            // 获取当前类实现的接口
            Class<?>[] currentInterfaces = clazz.getInterfaces();
            interfaces.addAll(Arrays.asList(currentInterfaces));
            // 递归检查父类
            clazz = clazz.getSuperclass();
        }
        return interfaces;
    }
}
