package com.kescoode.adk.net;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kesco on 15/3/1.
 */
public class YongConfig {
    public String baseUrl;
    public Map<String, String> headers;

    public Context context;

    private YongConfig(Builder builder) {
        baseUrl = builder.baseUrl;
        headers = builder.headers;
        context = builder.context;
    }

    /**
     * Configure builder
     */
    public static class Builder {
        private String baseUrl = "";
        private Map<String, String> headers = new HashMap<String, String>();

        private Context context = null;

        public Builder() {
            /* Empty */
        }

        /**
         * Global application shared value
         */
        public Builder baseUrl(String url) {
            baseUrl = url;
            return this;
        }

        /**
         * attach the context
         */
        public Builder attachContext(Context context) {
            this.context = context;
            return this;
        }

        /**
         * add header of the http request
         */
        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }

        /**
         * set the user agent of the http request
         */
        public Builder userAgent(String agent) {
            header("User-agent", agent);
            return this;
        }

        /**
         * set the default user agent from system
         */
        public Builder defaultAgent() {
            String userAgent = System.getProperty("http.agent");
            userAgent(userAgent);
            return this;
        }
        
        /**
         * create the configure
         */
        public YongConfig create() {
            if (null == context) {
                throw new RuntimeException("Dame library must have a context config.");
            }

            return new YongConfig(this);
        }
    }
}
