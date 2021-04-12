package com.itmuch.myusercenter.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 不加这个运行时反射拿不到
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {
    String value();
}
