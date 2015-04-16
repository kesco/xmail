package com.kescoode.adk.net;

import android.util.SparseArray;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 记录Target相关API回调
 *
 * @author Kesco Lin
 */
/* pacakge */ class TargetInfo {
    private final SparseArray<Map<String, Method>> doneCalls;
    private final SparseArray<Map<String, Method>> failCalls;

    private final Class<?> target;

    public TargetInfo(Class<?> target) {
        this.target = target;
        this.doneCalls = new SparseArray<>();
        this.failCalls = new SparseArray<>();
    }

    public void addDoneCall(Class<?> api, String apiMethod, Method method) {
        Map<String, Method> map = doneCalls.get(api.hashCode());
        if (null == map) {
            map = new HashMap<>();
            doneCalls.append(api.hashCode(), map);
        }
        map.put(apiMethod, method);
    }

    public Method getDoneCall(Class<?> api, String apiMethod) {
        Map<String, Method> map = doneCalls.get(api.hashCode());
        if (map == null) {
            map = new HashMap<>();
            doneCalls.append(api.hashCode(), map);
            return null;
        }
        Method method = map.get(apiMethod);
        if (null == method) {
            return map.get("#");
        }
        return method;
    }

    public void addFailCall(Class<?> api, String apiMethod, Method method) {
        Map<String, Method> map = failCalls.get(api.hashCode());
        if (null == map) {
            map = new HashMap<>();
            failCalls.append(api.hashCode(), map);
        }
        map.put(apiMethod, method);
    }

    public Method getFailCall(Class<?> api, String apiMethod) {
        Map<String, Method> map = failCalls.get(api.hashCode());
        if (map == null) {
            map = new HashMap<>();
            failCalls.append(api.hashCode(), map);
            return null;
        }
        Method method = map.get(apiMethod);
        if (null == method) {
            return map.get("#");
        }
        return method;
    }

}
