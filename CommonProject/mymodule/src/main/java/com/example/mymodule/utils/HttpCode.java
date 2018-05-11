package com.example.mymodule.utils;

/**
 * Created by 63062 on 2017/10/14.
 */

public interface HttpCode {
    int EXCEPTION_TIME_OUT = 10001;//超时

    int EXCEPTION_NO_CONNECT = 10002;//没有网络

    int EXCEPTION_DATA_GET = 10003;

    int EXCEPTION_OTHER = 10004;

    int EXCEPTION_AUTHORIZATION_EXPIRES= 10005;

    int EXCEPTION_DATA_PARSE = 10006;
}
