package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindingsOfAuditDTO {
    /**
     * 请求唯一id
     */
    private Long logId;

    /**
     * 审核结果，可取值：合规、不合规、疑似、审核失败
     */
    private String conclusion;

    /**
     * 审核结果类型，可取值1.合规，2.不合规，3.疑似，4.审核失败
     */
    private int conclusionType;

}
