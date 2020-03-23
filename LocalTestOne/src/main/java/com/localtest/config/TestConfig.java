package com.localtest.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

public class TestConfig {

    //该类用来装一些变量，这些变量与application.properties对应
    public static String loginUrl;
    public static String updateUserInfoUrl;
    public static String getUserListUrl;
    public static String getUserInfoUrl;
    public static String addUserUrl;

    /*将HttpClient也定义在这，因为每一个Test里边将来我们调用接口的时候都有HttpClient，所以在这里先定义以下*/
    public static CloseableHttpClient closeableHttpClient;

    /*因为cookies也是很常用到，所以也先定义下*/
    public static CookieStore cookieStore;
}