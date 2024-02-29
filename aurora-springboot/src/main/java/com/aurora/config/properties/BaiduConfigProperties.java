package com.aurora.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author liking
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "baidu")
public class BaiduConfigProperties {

    private String clientId;

    private String clientSecret;

    private String appId;

    private String securityKey;
}
