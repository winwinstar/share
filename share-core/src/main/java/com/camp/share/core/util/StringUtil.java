package com.camp.share.core.util;

import java.util.Map;

/**
 * Created by winstar on 2017/9/10.
 */
public class StringUtil {

    public static boolean isEmpty(String param) {
        return param == null || param.length() == 0;
    }

    public static boolean isNotEmpty(String param) {
        return param != null && param.length() > 0;
    }

    public static boolean isAnyEmpty(String... params) {
        for (String param : params) {
            if (isEmpty(param)) {
                return true;
            }
            continue;
        }
        return false;
    }

    public static String urlAppend(Map map) {
        String params = "";
        for (Object key : map.keySet()) {
            params += key.toString() + "=" + map.get(key.toString()) + "&";
        }
        return params.substring(0,params.lastIndexOf("&"));
    }
}
