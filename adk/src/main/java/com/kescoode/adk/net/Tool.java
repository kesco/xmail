package com.kescoode.adk.net;

import java.util.Map;

/**
 * Created by kesco on 15/3/2.
 */
/* package */ class Tool {
    private Tool() {
        /* Empty */
    }

    /* package */
    static <T> void validateApiInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("Only interface endpoint definitions are supported.");
        }

        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("Interface definitions must not extend other interfaces.");
        }
    }

    /* package */
    static String buildQueries(Map<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder = builder.append("?");
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append("&");
            }
            builder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }

        return builder.toString();
    }

    /* package */
    static String buildPaths(String url, String key, String value) {
        return url = url.replace("<" + key + ">", value);
    }

}
