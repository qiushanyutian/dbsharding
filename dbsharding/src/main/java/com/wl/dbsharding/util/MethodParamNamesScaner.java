package com.wl.dbsharding.util;

import ognl.Ognl;
import ognl.OgnlException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanglei on 2016/9/28.
 */
//引用至：https://gist.github.com/wendal/2011728，用于解析方法的形参名称

public class MethodParamNamesScaner {

    private static List<String> getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        List<String> parameterNames = new ArrayList<>();

        for (Parameter parameter : parameters) {
            if (!parameter.isNamePresent()) {
                throw new IllegalArgumentException("Parameter names are not present!");
            }

            String parameterName = parameter.getName();
            parameterNames.add(parameterName);
        }

        return parameterNames;
    }


    /**
     * @param @param  key
     * @param @param  jp
     * @param @return
     * @return String
     * @throws
     * @Title: getKeyNameFromParam
     * @Description: 获得组合后的KEY值
     */
    public static String getKeyNameFromParam(String key, Object []params, Method method) {
        if (!key.contains("$")) {
            return key;
        }

        String regexp = "\\$\\{[^\\}]+\\}";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(key);
        List<String> names = new ArrayList<String>();
        try {
            while (matcher.find()) {
                names.add(matcher.group());
            }
            key = executeNames(key, names, params, method);
        } catch (Exception e) {
//            log.error("Regex Parse Error!", e);
        }


        return key;
    }

    /**
     * @param @param  key
     * @param @param  names
     * @param @param  jp
     * @param @return
     * @param @throws OgnlException
     * @return String
     * @throws
     * @Title: executeNames
     * @Description: 对KEY中的参数进行替换
     */
    private static String executeNames(String key, List<String> names, Object[] params, Method method) throws OgnlException {
        if (names == null || names.size() == 0) {
            return key;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        List<String> refParamNames = getParameterNames(method);
        for (int i = 0; i < refParamNames.size(); i++) {
            map.put(refParamNames.get(i), params[i]);
        }

        for (String name : names) {
            String temp = name.substring(2);
            temp = temp.substring(0, temp.length() - 1);
            key = myReplace(key, name, String.valueOf(Ognl.getValue(temp, map)));
        }

        return key;
    }

    /**
     * @param @param  src
     * @param @param  from
     * @param @param  to
     * @param @return
     * @return String
     * @throws
     * @Title: myReplace
     * @Description: 不依赖Regex的替换，避免$符号、{}等在String.replaceAll方法中当做Regex处理时候的问题。
     */
    private static String myReplace(String src, String from, String to) {
        int index = src.indexOf(from);
        if (index == -1) {
            return src;
        }

        return src.substring(0, index) + to + src.substring(index + from.length());
    }
}
