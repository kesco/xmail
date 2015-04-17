package com.kescoode.adk.net;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Service动态代理Handler
 *
 * @author Kesco Lin
 */
/* package */  class ServiceHandler implements InvocationHandler {
    private final Net net;
    private final Class<?> api;
    private final Object target;

    public ServiceHandler(Net net, Class<?> api, Object target) {
        this.net = net;
        this.api = api;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RequestBuilder builder = new RequestBuilder(net, target, api);
        builder.init(method);

        return builder.build(args);
    }

}
