package com.kescoode.adk.net;


import com.kescoode.adk.net.annotations.*;
import com.kescoode.adk.net.volley.Request;
import com.kescoode.adk.net.volley.Response;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Content Type为MultiPart的Service Model
 *
 * @author Kesco Lin
 */
public class MultiPartServiceModel extends ServiceModel {
    private final Map<String, String> parts = new HashMap<>();

    public MultiPartServiceModel(Net net, Method method) {
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
            } else if (Parts.class == type) {
                String[] tmpParts = ((Parts) methodAnnotation).value();
                for (String tmpPart : tmpParts) {
                    part(tmpPart);
                }
            }
        }
    }

    public void part(String part) {
        int colon = part.indexOf(':');
        if (colon == -1 || colon == 0 || colon == part.length() - 1) {
            throw new IllegalArgumentException(String.
                    format("@Parts value must be in the form \"Name: Value\". Found: \"%s\"", part));
        }
        String key = part.substring(0, colon);
        String value = part.substring(colon + 1).trim();
        parts.put(key, value);
    }

    @Override
    public ContentType.Type contentType() {
        return ContentType.Type.MULTIPART;
    }

    @Override
    public BaseRequest buildRequest(Response.Listener<Object> listener, Response.ErrorListener errorListener, Object... args) {
        Map<String, String> tmpHeaders = new LinkedHashMap<>(net.config.headers);
        tmpHeaders.putAll(headers());

        Map<String, String> params = new HashMap<>(parts);
        Map<String, String> paths = new LinkedHashMap<>();
        Map<String, String> queries = new LinkedHashMap<>();
        Map<String, File> fileMap = new LinkedHashMap<>();

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
            } else if (Part.class == clazz) {
                Class<?> argClazz = arg.getClass();
                if (File.class == argClazz) {
                    fileMap.put(((Part) annotation).value(), (File) arg);
                } else {
                    params.put(((Param) annotation).value(), String.valueOf(arg));
                }
            } else if (PartsMap.class == clazz) {
                // TODO: 添加
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

        MultiPartRequest r = new MultiPartRequest(httpMethod, desUrl, listener, errorListener);
        r.setHeaders(tmpHeaders);
        r.setContentType(contentType().toString());
        r.setParams(params);
        r.setFiles(fileMap);

        return r;
    }
}
