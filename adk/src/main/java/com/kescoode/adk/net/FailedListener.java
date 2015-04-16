package com.kescoode.adk.net;

import com.kescoode.adk.net.volley.Response;
import com.kescoode.adk.net.volley.VolleyError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 错误请求反射回调监听器
 *
 * @author Kesco Lin
 */
class FailedListener implements Response.ErrorListener {

    private final Object target;
    private final Method method;

    public FailedListener(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (null != method) {
            try {
                method.invoke(target, new NetError(error));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("%s method can not be private.", method.getName()), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(String.format("%s method throw %s", method.getName(), e.getCause()));
            }
        }
    }
}
