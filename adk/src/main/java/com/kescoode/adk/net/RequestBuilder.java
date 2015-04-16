package com.kescoode.adk.net;

import android.util.Log;
import com.kescoode.adk.net.annotations.Done;
import com.kescoode.adk.net.annotations.Fail;
import com.kescoode.adk.net.volley.Response;

import java.lang.reflect.Method;

/**
 * 根据反射构建相应的Volley Request.
 *
 * @author Kesco Lin
 */
/* package */ class RequestBuilder {
    private final Net net;
    private final Class<?> api;
    private final Object target;
    private TargetInfo targetInfo;
    private Method apiMethod;

    private ServiceModel model;

    private long startTime;
    private long endTime;

    public RequestBuilder(Net net, Object target, Class<?> api) {
        startTime = System.currentTimeMillis();

        this.net = net;
        this.api = api;
        this.target = target;
    }

    public void init(Method method) {
        model = net.getService(target.getClass(), method);
        if (model == null) {
            model = ServiceModel.create(net, method);
            model.analyse();
            net.addService(target.getClass(), method, model);
        }

        /* 分析Target */
        targetInfo = net.getTarget(target.getClass());
        if (null == targetInfo) {
            net.addTarget(target.getClass());
            targetInfo = net.getTarget(target.getClass());
            Method[] methods = target.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(Done.class)) {
                    Done annotation = m.getAnnotation(Done.class);
                    targetInfo.addDoneCall(annotation.value(), annotation.method(), m);
                } else if (m.isAnnotationPresent(Fail.class)) {
                    Fail annotation = m.getAnnotation(Fail.class);
                    targetInfo.addFailCall(annotation.value(), annotation.method(), m);
                }
            }
        }

        apiMethod = method;
    }

    public Object build(Object... args) {
        Method doneMethod = targetInfo.getDoneCall(api, apiMethod.getName());
        Method failMethod = targetInfo.getFailCall(api, apiMethod.getName());

        if (doneMethod == null || failMethod == null) {
            throw new IllegalArgumentException(String.format("API: %s has none callback method", api.getName()));
        }

        Class<?>[] classes = doneMethod.getParameterTypes();
        Class<?> callClazz;
        if (1 == classes.length) {
            callClazz = classes[0];
        } else {
            throw new IllegalArgumentException(String.format("CallBack Method: \"%s\" must be 1 parameter", doneMethod.getName()));
        }
        Response.Listener<Object> l = new DoneListener(target, doneMethod, callClazz);
        Response.ErrorListener failedListener = new FailedListener(target, failMethod);

        endTime = System.currentTimeMillis();
        Log.e("Dame Net", String.format("building request %s costs %d", api.getSimpleName(), endTime - startTime));
        return net.addRequest(model.buildRequest(l, failedListener, args));
    }
}
