package com.degaltseva.carrental.util;

import jakarta.servlet.http.HttpServletRequest;

public class PathUtil {

    public static String getPathParameter(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }
        return pathInfo.substring(1);
    }

    public static Long getPathId(HttpServletRequest request) {
        String param = getPathParameter(request);
        if (param == null) {
            return null;
        }
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
