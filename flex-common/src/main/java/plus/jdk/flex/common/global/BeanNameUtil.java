package plus.jdk.flex.common.global;

public class BeanNameUtil {
    public static String decapitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        if (name.length() == 1) {
            return name.toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        Boolean capitalizeNext = null;
        for (char c : name.toCharArray()) {
            // 检查字符是否是字母或数字
            if (!Character.isLetterOrDigit(c)) {
                capitalizeNext = true;
                continue;
            }
            if(capitalizeNext != null && capitalizeNext){
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            }
            result.append(c);
        }
        return result.substring(0, 1).toLowerCase() + result.substring(1);
    }
}
