package com.kescoode.adk.net;

import com.kescoode.adk.net.annotations.*;
import com.kescoode.adk.net.volley.Request;
import com.kescoode.adk.net.volley.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Content Type
 */
public class UrlEncodedServiceModel extends ServiceModel {
    private Map<String, String> params = new LinkedHashMap<>();

    public UrlEncodedServiceModel(Net net, Method method) {
        super(net, method);
    }

    @Override
    protected void analyseExecutor() {
        for (Annotation methodAnnotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = methodAnnotation.annotationType();
            if (Post.class == type) {
                setHttpRequestMethod(Request.Method.POST, ((Post) methodAnnotation).value(),
                        ((Post) methodAnnotation).override());
            } else if (Put.class == type) {
                setHttpRequestMethod(Request.Method.PUT, ((Put) methodAnnotation).value(),
                        ((Put) methodAnnotation).override());
            } else if (Headers.class == type) {
                String[] headers = ((Headers) methodAnnotation).value();
                for (String header : headers) {
                    header(header);
                }
            } else if (Params.class == type) {
                String[] params = ((Params) methodAnnotation).value();
                for (String param : params) {
                    param(param);
                }
            }
        }
    }

    public void param(String param) {
        int colon = param.indexOf(':');
        if (colon == -1 || colon == 0 || colon == param.length() - 1) {
            throw new IllegalArgumentException(String.
                    format("@Params value must be in the form \"Name: Value\". Found: \"%s\"", param));
        }
        String key = param.substring(0, colon);
        String value = param.substring(colon + 1).trim();
        this.params.put(key, value);
    }

    public Map<String, String> params() {
        return params;
    }

    @Override
    public BaseRequest buildRequest(Response.Listener<Object> listener, Response.ErrorListener errorListener, Object... args) {
        Map<String, String> tmpHeaders = new LinkedHashMap<>(net.config.headers);
        tmpHeaders.putAll(headers());

        Map<String, String> temParams = new LinkedHashMap<>(params);
        Map<String, String> paths = new LinkedHashMap<>();
        Map<String, String> queries = new LinkedHashMap<>();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Annotation annotation = paramsAnnotation[i];
            Class<?> clazz = annotation.annotationType();
            if (Header.class == clazz) {
                tmpHeaders.put(((Header) annotation).value(), String.valueOf(arg));
            } else if (Path.class == clazz) {
                paths.put(((Path) annotation).value(), String.valueOf(arg));
            } else if (Query.class == clazz) {
                queries.put(((Query) annotation).value(), String.valueOf(arg));
            } else if (Param.class == clazz) {
                temParams.put(((Param) annotation).value(), String.valueOf(arg));
            }
        }

        String desUrl;
        if (urlOverride) {
            desUrl = url;
        } else {
            desUrl = net.getConfig().baseUrl + url;
        }
        if (!queries.isEmpty()) {
            desUrl = desUrl + Tool.buildQueries(queries);
        }
        if (!paths.isEmpty()) {
            for (Map.Entry<String, String> entry : paths.entrySet()) {
                desUrl = Tool.buildPaths(desUrl, entry.getKey(), entry.getValue());
            }
        }

        BaseRequest r = new BaseRequest(httpMethod, desUrl, listener, errorListener);
        r.setHeaders(tmpHeaders);
        r.setParams(temParams);
        r.setContentType(contentType().toString());

        return r;
    }
}
