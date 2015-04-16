package com.kescoode.adk.net;

/**
 * Created by kesco on 15/2/28.
 */
public class Yong {
    
    private Net net = null;

    private Yong() {
        /* Empty */
    }
    
    public static Yong get() {
        return SingleHolder.singleton;
    }
    
    private static class SingleHolder {
        private static final Yong singleton = new Yong();
    }
    
    public static void net(YongConfig config) {
        get().net = new Net(config);
    }
    
    private void hasNet() {
        if (null == net) {
            throw new RuntimeException("Has not initial net module.");
        }
    }
    
    public <T> T async(Class<T> api, Object target) {
        hasNet();
        if (null == api) {
            throw new IllegalArgumentException("API Clazz cannot be null.");
        }
        return net.create(api,target);
    }

}
