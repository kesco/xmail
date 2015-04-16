package com.kescoode.adk.net.annotations;

import java.lang.annotation.*;

/**
 * MultiPart定义参数，可以是基本类型、字符串和File
 *
 * @author Kesco Lin
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Part {
    String value();
}
