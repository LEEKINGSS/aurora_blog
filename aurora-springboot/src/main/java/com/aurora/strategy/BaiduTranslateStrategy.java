package com.aurora.strategy;

/**
 * author: liking
 * 百度翻译服务策略
 */
public interface BaiduTranslateStrategy {
    /**
     * 翻译文本
     */
    public String getTransResult(String query, String from, String to);
}
