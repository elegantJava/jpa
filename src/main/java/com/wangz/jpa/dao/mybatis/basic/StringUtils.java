package com.wangz.jpa.dao.mybatis.basic;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lianglongqun on 2017/6/12.
 */
public class StringUtils {

    public static String seperate(String[] array, String seperator) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            buffer.append(seperator + array[i]);
        }
        return buffer.toString();
    }

    /**
     * 格式化, 将字符串"{0}12{3}"中的"{0}", "{3}"替换成其他值.
     *
     * @param string
     * @return
     */
    public static String formatByMap(String string, Map<String, ?> map) {
        return formatByMap(string, map, true);
    }

    /**
     * 格式化, 将字符串"{0}12{3}"中的"{0}", "{3}"替换成其他值.
     *
     * @param string
     * @param replaceWhenValueNotExist 当值不存在时，是否要替换
     * @return
     */
    public static String formatByMap(String string, Map<String, ?> map, boolean replaceWhenValueNotExist) {
        String regex = "\\{[^\\{]*?\\}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String indexStringWithBracket = matcher.group();
            String indexString = removeStartEnd(indexStringWithBracket, "{", "}");

            if(map.containsKey(indexString) || replaceWhenValueNotExist){
                String replacement = String.valueOf(map.get(indexString));
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();

    }

    /**
     * 格式化, 将字符串"{{0}}12{{}3}}"中的"{{0}}", "{{3}}"替换成其他值.
     *
     * @param string
     * @param replaceWhenValueNotExist 当值不存在时，是否要替换
     * @return
     */
    public static String formatTemplate(String string, Map<String, ?> map, boolean replaceWhenValueNotExist) {
        String regex = "\\{\\{[^\\{]*?\\}\\}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String indexStringWithBracket = matcher.group();
            String indexString = removeStartEnd(indexStringWithBracket, "{{", "}}");

            if(map.containsKey(indexString) || replaceWhenValueNotExist){
                Object obj = map.get(indexString);
                String replacement = obj == null ? "" : String.valueOf(obj);
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();

    }


    /**
     * 替换url中的可替换值{name}
     */
    public static String uri(String string, Object... replaceStrings) {
        String regex = "\\{[^\\{]*?\\}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        StringBuffer sb = new StringBuffer();
        int index = 0;
        while (matcher.find()) {
            String replacement = String.valueOf(replaceStrings[index++]);
            matcher.appendReplacement(sb,
                    Matcher.quoteReplacement(replacement));

        }
        matcher.appendTail(sb);
        return sb.toString();

    }

    public static String removeStartEnd(String string, String startString,
                                        String endString) {
        string = org.apache.commons.lang3.StringUtils.removeStart(string, startString);
        string = org.apache.commons.lang3.StringUtils.removeEnd(string, endString);
        return string;
    }

    public static boolean contains(String[] array, String valueToFind) {

        if(array == null){
            return false;
        }

        for (String s : array) {
            if(ObjectUtils.equals(s, valueToFind)){
                return true;
            }
        }

        return false;
    }

    public static String seperate(Collection<?> list, String string) {

        StringBuilder builder = new StringBuilder();

        boolean isFisrt = true;
        if (list != null) {
            for (Object element : list) {
                if(! isFisrt){
                    builder.append(string);
                }

                builder.append(element);

                isFisrt = false;
            }
        }

        return builder.toString();
    }

    public static String seperate(Collection<?> list, String string, String start, String end) {

        StringBuilder builder = new StringBuilder();

        boolean isFisrt = true;
        if (list != null) {
            for (Object element : list) {
                if(! isFisrt){
                    builder.append(string);
                }

                builder.append(start + element + end);

                isFisrt = false;
            }
        }

        return builder.toString();

    }

    public static boolean isNullOrEmpty(String toTest) {
        return toTest == null || toTest.length() == 0;
    }

    public static boolean isNotNullAndNotEmpty(String string) {
        return ! isNullOrEmpty(string);
    }

    public static boolean isNotEmpty(String string) {
        return ! isNullOrEmpty(string);
    }

}
