package plus.jdk.flex.common.global;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonProvider {

    /**
     * 存储单例实例的映射表。
     * 使用 ConcurrentHashMap 来保证线程安全。
     */
    static final Map<Class<?>, Object> INSTANCE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取指定类的单例实例。
     * @return 指定类的单例实例。
     */

    @SneakyThrows
    public static <T> T refer(Class<T> clazz)  {
        if(INSTANCE_MAP.containsKey(clazz)) {
            return (T) INSTANCE_MAP.get(clazz);
        }
        T instance = null;
        synchronized (SingletonProvider.class) {
            if(INSTANCE_MAP.containsKey(clazz)) {
                return (T) INSTANCE_MAP.get(clazz);
            }
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            INSTANCE_MAP.put(clazz, instance);
        }
        return instance;
    }
}
