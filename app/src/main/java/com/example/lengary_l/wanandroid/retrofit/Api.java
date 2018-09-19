package com.example.lengary_l.wanandroid.retrofit;

/**
 * Created by CoderLengary
 */


class Api {

    //Base API.
    public static final String API_BASE = "http://www.wanandroid.com/";

    //获取文章列表
    public static final String ARTICLE_LIST = API_BASE + "article/list/";

    //获得文章分类
    public static final String CATEGORIES = API_BASE + "tree/json";

    //获得首页Banner
    public static final String BANNER = API_BASE + "banner/json";


    //登陆
    public static final String LOGIN = API_BASE + "user/login/";

    //注册
    public static final String REGISTER = API_BASE + "user/register";


    //获得查询的文章
    public static final String QUERY_ARTICLES = API_BASE + "article/query/";

    //热搜词
    public static final String HOT_KEY = API_BASE + "hotkey/json";

    //收藏文章
    public static final String COLLECT_ARTICLE = API_BASE + "lg/collect/";

    //取消收藏文章
    public static final String CANCEL_COLLECTING_ARTICLE = API_BASE + "lg/uncollect_originId/";

    //获得收藏文章的列表
    public static final String GET_FAVORITE_ARTICLES = API_BASE + "lg/collect/list/";


}
