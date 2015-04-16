package com.kescoode.adk.net;

import com.kescoode.adk.net.volley.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 成功请求时的反射回调监听器
 *
 * @author Kesco Lin
 */
class DoneListener implements Response.Listener<Object> {
    private final Class<?> clazz;
    private final Object target;
    private final Method method;

    public DoneListener(Object target, Method method, Class<?> clazz) {
        this.clazz = clazz;
        this.target = target;
        this.method = method;
    }

    @Override
    public void onResponse(Object response) {
        if (null != method) {
            try {
                method.invoke(target, response);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("%s method can not be private.", method.getName()), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(String.format("%s method throw %s", method.getName(), e.getCause()),e.getCause());
            }
        }
    }

    public Class<?> getType() {
        return clazz;
    }
}
