package com.kescoode.adk.net;

import com.kescoode.adk.net.annotations.*;
import com.kescoode.adk.net.volley.Request;
import com.kescoode.adk.net.volley.Response;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP API接口Model
 *
 * @author Kesco Lin
 */
/* package */ class ServiceModel {
    protected final Net net;
    protected final Method method;
    protected int httpMethod;
    protected String url;
    protected boolean urlOverride;
    private Map<String, String> headers = new LinkedHashMap<>();
    protected Annotation[] paramsAnnotation;


    public ServiceModel(Net net, Method method) {
        this.net = net;
        this.method = method;
    }

    public void analyse() {
        analyseExecutor();
        analyseParameters();

        if (int.class != method.getReturnType()) {
            throw new IllegalArgumentException(String.format("The type of the method: \"%s\" return must be Integer.",
                    method.getName()));
        }
    }

    protected void analyseExecutor() {
        for (Annotation methodAnnotation : method.getDeclaredAnnotations()) {
            Class<? extends Annotation> type = methodAnnotation.annotationType();
            if (Get.class == type) {
                setHttpRequestMethod(Request.Method.GET, ((Get) methodAnnotation).value(),
                        ((Get) methodAnnotation).override());
            } else if (Delete.class == type) {
                setHttpRequestMethod(Request.Method.DELETE, ((Delete) methodAnnotation).value(),
                        ((Delete) methodAnnotation).override());
            } else if (Head.class == type) {
                setHttpRequestMethod(Request.Method.HEAD, ((Head) methodAnnotation).value(),
                        ((Head) methodAnnotation).override());
            } else if (Options.class == type) {
                setHttpRequestMethod(Request.Method.OPTIONS, ((Options) methodAnnotation).value(),
                        ((Options) methodAnnotation).override());
            } else if (Patch.class == type) {
                setHttpRequestMethod(Request.Method.PATCH, ((Patch) methodAnnotation).value(),
                        ((Patch) methodAnnotation).override());
            } else if (Trace.class == type) {
                setHttpRequestMethod(Request.Method.TRACE, ((Trace) methodAnnotation).value(),
                        ((Trace) methodAnnotation).override());
            } else if (Headers.class == type) {
                String[] headers = ((Headers) methodAnnotation).value();
                for (String header : headers) {
                    header(header);
                }
            }
        }
    }

    protected void setHttpRequestMethod(int method, String url, boolean override) {
        this.httpMethod = method;
        this.url = url;
        this.urlOverride = override;
    }

    protected void header(String header) {
        int colon = header.indexOf(':');
        if (colon == -1 || colon == 0 || colon == header.length() - 1) {
            throw new IllegalArgumentException(String.
                    format("@Headers value must be in the form \"Name: Value\". Found: \"%s\"", header));
        }
        String headerName = header.substring(0, colon);
        String headerValue = header.substring(colon + 1).trim();
        this.headers.put(headerName, headerValue);
    }

    protected void analyseParameters() {
        argsAnnotation(method.getParameterAnnotations());
    }

    public void argsAnnotation(Annotation[][] args) {
        this.paramsAnnotation = new Annotation[args.length];
        for (int i = 0; i < args.length; i++) {
            Annotation[] arg = args[i];
            int length = arg.length;
            if (0 == length) {
                throw new IllegalArgumentException("Parameter has no annotation.");
            } else if (1 == length) {
                this.paramsAnnotation[i] = arg[0];
            } else if (1 < length) {
                throw new IllegalArgumentException("Parameter annotation must be only one in one argument.");
            }
        }
    }

    public BaseRequest buildRequest(Response.Listener<Object> listener, Response.ErrorListener errorListener, Object... args) {
        Map<String, String> tmpHeaders = new LinkedHashMap<>(net.config.headers);
        tmpHeaders.putAll(headers);

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
        return r;
    }

    /**
     * 直接返回URLEncoded，虽然对于ServiceModel没什么意义
     *
     * @return {@link ContentType.Type}
     */
    public ContentType.Type contentType() {
        return ContentType.Type.FORM_URLENCODED;
    }

    /**
     * 获取HTTP Request的常量Header
     *
     * @return Headers {@link Map}
     */
    public Map<String, String> headers() {
        return this.headers;
    }

    /**
     * 获取HTTP Method
     *
     * @return 返回 {@link Request.Method} 里的常量
     */
    public int httpMethod() {
        return httpMethod;
    }

    /**
     * 获取HTTP请求地址
     *
     * @return url
     */
    public String url() {
        return url;
    }

    /**
     * HTTP请求地址是否重置全局地址前缀
     *
     * @return override
     */
    public boolean isUrlOverride() {
        return urlOverride;
    }


    /**
     * 分析ContentType注解，生成不同的Model
     *
     * @param method 业务API Method
     * @return 相应的ServiceModel
     */
    public static ServiceModel create(Net net,Method method) {
        ContentType contentTypeAnnotation = method.getAnnotation(ContentType.class);
        if (contentTypeAnnotation == null) {
            Post postAnnotation = method.getAnnotation(Post.class);
            if (postAnnotation != null) {
                return new UrlEncodedServiceModel(net,method);
            }
            Put putAnnotation = method.getAnnotation(Put.class);
            if (putAnnotation != null) {
                return new UrlEncodedServiceModel(net,method);
            }
            return new ServiceModel(net,method);
        }
        ContentType.Type contentType = contentTypeAnnotation.value();
        if (contentType == ContentType.Type.JSON) {
            return new JsonServiceModel(net,method);
        } else if (contentType == ContentType.Type.MULTIPART) {
            return new MultiPartServiceModel(net,method);
        } else {
            return new UrlEncodedServiceModel(net,method);
        }
    }
}
