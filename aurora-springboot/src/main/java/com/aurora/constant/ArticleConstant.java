package com.aurora.constant;

/**
 * @author liking
 * 文章模块相关常量
 */
public interface ArticleConstant {
    /**
     * 状态值 1公开 2私密 3草稿
     */
    public static final Integer ARTICLE_STATUS_PUBLIC = 1;

    public static final Integer ARTICLE_STATUS_PRIVATE = 2;

    public static final Integer ARTICLE_STATUS_DRAFT = 3;

    /**
     * 每分钟阅读文字个数
     */
    public static final Integer WORDS_PER_MINUTE = 200;
}
