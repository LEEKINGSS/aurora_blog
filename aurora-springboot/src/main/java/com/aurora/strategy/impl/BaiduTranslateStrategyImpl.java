package com.aurora.strategy.impl;

import com.aurora.config.properties.BaiduConfigProperties;
import com.aurora.strategy.BaiduTranslateStrategy;
import com.aurora.util.HttpUtil;
import com.aurora.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.aurora.constant.BaiduTranslateConstant.TRANS_API_HOST;

/**
 * author: liking
 * 百度翻译服务策略实现
 */
@Service("baiduTranslateStrategyImpl")
public class BaiduTranslateStrategyImpl implements BaiduTranslateStrategy {
    @Autowired
    private BaiduConfigProperties baiduConfigProperties;

    public String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpUtil.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        String appid = baiduConfigProperties.getAppId();
        String securityKey = baiduConfigProperties.getSecurityKey();
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", baiduConfigProperties.getAppId());

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名-加密前的原文
        String src = appid + query + salt + securityKey;
        params.put("sign", MD5Util.md5(src));

        return params;
    }
}
