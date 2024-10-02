package plus.jdk.flex.common.global;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * class loader加载基础类
 */
public class ClassLoaderUtils {

    /**
     * 存储基本类型的Class对象的集合。
     */
    private static final Set<Class<?>> PRIMITIVE_SET = new HashSet<>();

    /**
     * 获取当前线程的类加载器。
     * 如果当前线程的上下文类加载器为null，则尝试获取ClassLoaderUtils类的类加载器。
     * 如果仍然为null，则返回系统类加载器。
     * @return 当前线程的类加载器。
     */
    public static ClassLoader getCurrentClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoaderUtils.class.getClassLoader();
        }

        return cl == null ? ClassLoader.getSystemClassLoader() : cl;
    }

    /**
     * 获取指定类的类加载器。
     * 如果当前线程的上下文类加载器不为null，则返回该类加载器；否则如果指定的类不为null，则返回该类的类加载器；否则返回系统类加载器。
     * @param clazz 指定类
     * @return 类加载器
     */
    public static ClassLoader getClassLoader(Class<?> clazz) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            return loader;
        } else if (clazz != null) {
            loader = clazz.getClassLoader();
            return loader != null ? loader : clazz.getClassLoader();
        } else {
            return ClassLoader.getSystemClassLoader();
        }
    }

    /**
     * 根据类名获取对应的 Class 对象。
     * @param className 类名字符串。
     * @return 对应的 Class 对象。
     * @throws ClassNotFoundException 如果无法找到指定类名的类。
     */
    public static Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true);
    }

    /**
     * 根据类名获取对应的 Class 对象。
     * @param className 类名
     * @param initialize 是否初始化该类
     * @return 对应的 Class 对象
     * @throws ClassNotFoundException 如果找不到指定的类
     */
    public static Class<?> forName(String className, boolean initialize) throws ClassNotFoundException {
        return Class.forName(className, initialize, getCurrentClassLoader());
    }

    /**
     * 根据给定的类名和类加载器，返回对应的 Class 对象。
     * @param className 要加载的类的名称。
     * @param cl 类加载器。
     * @return 对应的 Class 对象。
     * @throws ClassNotFoundException 如果无法找到指定的类。
     */
    public static Class<?> forName(String className, ClassLoader cl) throws ClassNotFoundException {
        return Class.forName(className, true, cl);
    }

    /**
     * 创建指定类的新实例。
     * @param clazz 要创建实例的类对象。
     * @return 新创建的实例对象。
     * @throws Exception 如果无法创建实例，例如类没有默认构造器或构造器不可访问。
     */
    public static <T> T newInstance(Class<T> clazz) throws Exception {
        if (PRIMITIVE_SET.contains(clazz)) {
            return null;
        } else if (!isCanInstance(clazz)) {
            return null;
        } else {
            Constructor<?> defaultConstructor;
            Object var3;
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                Constructor<?>[] constructorList = clazz.getDeclaredConstructors();
                defaultConstructor = null;
                Constructor<?>[] arr$ = constructorList;
                int len$ = constructorList.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Constructor<?> con = arr$[i$];
                    if (con.getParameterTypes().length == 1) {
                        defaultConstructor = con;
                        break;
                    }
                }

                if (defaultConstructor != null) {
                    if (defaultConstructor.isAccessible()) {
                        return (T) defaultConstructor.newInstance(null);
                    } else {
                        try {
                            defaultConstructor.setAccessible(true);
                            var3 = defaultConstructor.newInstance(null);
                        } finally {
                            defaultConstructor.setAccessible(false);
                        }

                        return (T) var3;
                    }
                } else {
                    throw new Exception("The " + clazz.getCanonicalName() + " has no default constructor!");
                }
            } else {
                try {
                    return clazz.newInstance();
                } catch (Exception var17) {
                    Exception e = var17;
                    defaultConstructor = clazz.getDeclaredConstructor();
                    if (defaultConstructor.isAccessible()) {
                        throw new Exception("The " + clazz.getCanonicalName() + " has no default constructor!", e);
                    } else {
                        try {
                            defaultConstructor.setAccessible(true);
                            var3 = defaultConstructor.newInstance();
                        } finally {
                            defaultConstructor.setAccessible(false);
                        }

                        return (T) var3;
                    }
                }
            }
        }
    }

    /**
     * 判断一个类是否可以被实例化。
     * @param clazz 要检查的类对象。
     * @return 如果该类不是数组类型且不是枚举类型，则返回 true；否则返回 false。
     */
    public static boolean isCanInstance(Class clazz) {
        return !clazz.isArray() && !clazz.isEnum();
    }

    static {
        PRIMITIVE_SET.add(Integer.class);
        PRIMITIVE_SET.add(Long.class);
        PRIMITIVE_SET.add(Float.class);
        PRIMITIVE_SET.add(Byte.class);
        PRIMITIVE_SET.add(Short.class);
        PRIMITIVE_SET.add(Double.class);
        PRIMITIVE_SET.add(Character.class);
        PRIMITIVE_SET.add(Boolean.class);
        PRIMITIVE_SET.add((new HashMap<>()).keySet().getClass());
        PRIMITIVE_SET.add((new TreeMap<>()).values().getClass());
        PRIMITIVE_SET.add((new ArrayList<>()).subList(0, 0).getClass());
    }
}
