package com.kescoode.adk.net;

import android.util.SparseArray;
import com.kescoode.adk.net.volley.Request;
import com.kescoode.adk.net.volley.RequestQueue;
import com.kescoode.adk.net.volley.toolbox.Volley;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Net Module
 *
 * @author Kesco Lin
 */
class Net {
    protected volatile YongConfig config;

    /**
     * 新的关联API Map
     */
    private final Map<String, ServiceModel> services = new HashMap<>();

    /**
     * 关联Target缓存，Target -> Api -> Method
     */
    private final SparseArray<TargetInfo> targetMap;

    private final RequestQueue queue;

    public Net(YongConfig config) {
        this.config = config;
        targetMap = new SparseArray<>();
        queue = Volley.newRequestQueue(config.context);
    }

    public void addService(Class<?> provider, Method service, ServiceModel model) {
        String key = provider.getCanonicalName() + "#" + service.getName();
        services.put(key, model);
    }

    public ServiceModel getService(Class<?> provider, Method service) {
        String key = provider.getCanonicalName() + "#" + service.getName();
        return services.get(key);
    }

    void addTarget(Class<?> target) {
        synchronized (targetMap) {
            targetMap.append(target.hashCode(), new TargetInfo(target));
        }
    }

    TargetInfo getTarget(Class<?> target) {
        synchronized (targetMap) {
            return targetMap.get(target.hashCode());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> api, Object target) {
        Tool.validateApiInterface(api);
        return (T) Proxy.newProxyInstance(api.getClassLoader(), new Class[]{api}, new ServiceHandler(this, api, target));
    }

    int addRequest(Request request) {
        int hash = request.hashCode();
        request.setTag(hash);
        queue.add(request);
        return hash;
    }

    public YongConfig getConfig() {
        return config;
    }

}
