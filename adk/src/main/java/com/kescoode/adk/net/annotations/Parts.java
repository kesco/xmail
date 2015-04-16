package com.kescoode.adk.net.annotations;

import java.lang.annotation.*;

/**
 * Created by kesco on 15/3/16.
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parts {
    String[] value();
}
