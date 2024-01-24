package com.aurora.strategy;

import com.aurora.model.dto.FindingsOfAuditDTO;

public interface BaiduAuditingStrategy {
    /**
     * 获取百度自动审核token
     * @return
     */
    String getAccessToken();

    /**
     * 文本审核
     * @param text
     * @return
     */
    FindingsOfAuditDTO textAudit(String text);

}
