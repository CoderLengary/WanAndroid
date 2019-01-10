package com.example.lengary_l.wanandroid.retrofit;

/**
 * Created by CoderLengary
 */


class Api {

    //Base API.
    static final String API_BASE = "http://www.wanandroid.com/";

    static final String ARTICLE_LIST = API_BASE + "article/list/";

    static final String CATEGORIES = API_BASE + "tree/json";

    static final String BANNER = API_BASE + "banner/json";

    static final String LOGIN = API_BASE + "user/login/";

    static final String REGISTER = API_BASE + "user/register";

    static final String QUERY_ARTICLES = API_BASE + "article/query/";

    static final String HOT_KEY = API_BASE + "hotkey/json";

    static final String COLLECT_ARTICLE = API_BASE + "lg/collect/";

    static final String CANCEL_COLLECTING_ARTICLE = API_BASE + "lg/uncollect_originId/";

    static final String GET_FAVORITE_ARTICLES = API_BASE + "lg/collect/list/";


}
