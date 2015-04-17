package com.kescoode.adk.net;

import com.kescoode.adk.net.annotations.*;
import com.kescoode.adk.net.volley.Request;
import com.kescoode.adk.net.volley.Response;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Content Type为JSON的Service Model
 *
 * @author Kesco Lin
 */
public class JsonServiceModel extends ServiceModel {

    public JsonServiceModel(Net net, Method method) {
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
            }
        }
    }

    @Override
    public ContentType.Type contentType() {
        return ContentType.Type.JSON;
    }

    @Override
    public BaseRequest buildRequest(Response.Listener<Object> listener, Response.ErrorListener errorListener, Object... args) {
        Map<String, String> tmpHeaders = new LinkedHashMap<>(net.config.headers);
        tmpHeaders.putAll(headers());

        Map<String, String> paths = new LinkedHashMap<>();
        Map<String, String> queries = new LinkedHashMap<>();
        Map<String, Object> jsonMap = new LinkedHashMap<>();

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
            } else if (JSON.class == clazz) {
                jsonMap.put(((JSON) annotation).value(), arg);
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
        r.setContentType(contentType().toString());
        r.setJson(new JSONObject(jsonMap));

        return r;
    }
}
