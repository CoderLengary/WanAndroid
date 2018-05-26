package com.example.lengary_l.wanandroid.retrofit;

public class Api {

    //This is the base API.
    public static final String API_BASE = "http://www.wanandroid.com/";

    //Get the article categories
    public static final String ARTICLE_CATEGORIES = API_BASE + "tree/json";


    //Get the article list from article categories
    public static final String ARTICLE_LIST = API_BASE + "article/list/";

    //Get the banner
    public static final String GET_BANNER = API_BASE + "banner/json";


    //Get project categories
    public static final String PROJECT_CATEGORIES = API_BASE + "project/tree/json";


    //Get the projects from projects categories
    public static final String PROJECT_LIST = API_BASE + "project/list/";

    //Login
    public static final String LOGIN = API_BASE + "user/login/";

    //Register
    public static final String REGISTER = API_BASE + "user/register";


    //Search articles
    public static final String QUERY_ARTICLES = API_BASE + "/article/query/";

}
