package com.aurora.strategy.impl;

import com.aurora.model.dto.FindingsOfAuditDTO;
import com.aurora.strategy.BaiduAuditingStrategy;
import com.aurora.util.HttpUtil;
import org.json.JSONObject;
import com.aurora.config.properties.BaiduConfigProperties;
import com.aurora.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static com.aurora.constant.RedisConstant.BAIDU_TOKEN;

@Service("baiduAuditingStrategyImpl")
public class BaiduAuditingStrategyImpl implements BaiduAuditingStrategy {

    @Autowired
    private BaiduConfigProperties baiduConfigProperties;

    @Autowired
    private RedisService redisService;

    /**
     * 获取百度自动审核token
     */
    public String getAccessToken() {
        // 查看Redis中是否存在token
        Object baiduToken = redisService.get(BAIDU_TOKEN);
        // 如果不存在则重新获取
        if (baiduToken == null) {
            // 获取token
            String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
            String getAccessTokenUrl = authHost
                    // 1. grant_type为固定参数
                    + "grant_type=client_credentials"
                    // 2. 官网获取的 API Key
                    + "&client_id=" + baiduConfigProperties.getClientId()
                    // 3. 官网获取的 Secret Key
                    + "&client_secret=" + baiduConfigProperties.getClientSecret();
            try {
                URL realUrl = new URL(getAccessTokenUrl);
                // 打开和URL之间的连接
                HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                for (String key : map.keySet()) {
                    System.err.println(key + "--->" + map.get(key));
                }
                // 定义 BufferedReader输入流来读取URL的响应
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String result = "";
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                /**
                 * 返回结果示例
                 */
                System.err.println("result:" + result);
                JSONObject jsonObject = new JSONObject(result);
                String access_token = jsonObject.getString("access_token");
                // 将token存入Redis中并设置失效时间为30天
                redisService.set(BAIDU_TOKEN, access_token, 2592000);
                return access_token;
            } catch (Exception e) {
                System.err.printf("获取token失败！");
                e.printStackTrace(System.err);
            }
            return null;
        }
        return (String) baiduToken;
    }

    /**
     * 百度自动文本审核
     */
    public FindingsOfAuditDTO textAudit(String text) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        FindingsOfAuditDTO findingsOfAuditDTO = new FindingsOfAuditDTO();
        try {
            // 请求参数
            String param = "text=" + URLEncoder.encode(text);
            // 获取token
            String accessToken = getAccessToken();
            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            findingsOfAuditDTO = com.alibaba.fastjson.JSONObject.parseObject(result, FindingsOfAuditDTO.class);
            return findingsOfAuditDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        findingsOfAuditDTO.setConclusionType(4);
        return findingsOfAuditDTO;
    }

}
