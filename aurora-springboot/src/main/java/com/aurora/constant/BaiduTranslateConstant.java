package com.aurora.constant;

/**
 * 百度翻译常量
 */
public interface BaiduTranslateConstant {

    /**
     * 请求超时时间
     */
    public static final int SOCKET_TIMEOUT = 10000; // 10S

    /**
     * GET请求
     */
    public static final String GET = "GET";

    /**
     * 百度翻译请求网址
     */
    public static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
}
