package com.kescoode.adk.net.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kesco on 15/3/4.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentType {
    Type value();

    public static enum Type {
        FORM_URLENCODED("application/x-www-form-urlencoded"), MULTIPART("multipart/form-data"),
        JSON("application/json"), XML("text/xml");

        private String content;

        Type(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
