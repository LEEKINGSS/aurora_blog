package com.aurora.constant;

public interface RedisConstant {

    long CODE_EXPIRE_TIME = 15 * 60;

    String USER_CODE_KEY = "code:";

    String BLOG_VIEWS_COUNT = "blog_views_count";

    /**
     * 文章浏览量
     */
    String ARTICLE_VIEWS_COUNT = "article_views_count";

    /**
     * 笔记浏览量
     */
    String NOTE_VIEWS_COUNT = "note_views_count";

    String WEBSITE_CONFIG = "website_config";

    String USER_AREA = "user_area";

    String VISITOR_AREA = "visitor_area";

    String ABOUT = "about";

    String UNIQUE_VISITOR = "unique_visitor";

    String LOGIN_USER = "login_user";

    String ARTICLE_ACCESS = "article_access:";

    // 百度token
    String BAIDU_TOKEN = "baidu_token";

}
