package com.example.lengary_l.wanandroid.retrofit;

import com.example.lengary_l.wanandroid.data.BannerData;
import com.example.lengary_l.wanandroid.data.CategoriesData;
import com.example.lengary_l.wanandroid.data.ArticlesData;
import com.example.lengary_l.wanandroid.data.FavoriteArticlesData;
import com.example.lengary_l.wanandroid.data.HotKeysData;
import com.example.lengary_l.wanandroid.data.LoginData;
import com.example.lengary_l.wanandroid.data.Status;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by CoderLengary
 */


public interface RetrofitService {

    @FormUrlEncoded
    @POST(Api.LOGIN)
    Observable<LoginData> login(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST(Api.REGISTER)
    Observable<LoginData> register(@Field("username") String username, @Field("password") String password,@Field("repassword") String repassword);

    @GET(Api.ARTICLE_LIST + "{page}/json")
    Observable<ArticlesData> getArticles(@Path("page") int page);

    @GET(Api.ARTICLE_LIST + "{page}/json")
    Observable<ArticlesData> getArticlesFromCatg(@Path("page") int page, @Query("cid") int cid);

    @GET(Api.CATEGORIES)
    Observable<CategoriesData> getCategories();

    @POST(Api.QUERY_ARTICLES + "{page}/json")
    Observable<ArticlesData> queryArticles(@Path("page") int page, @Query("k") String k);

    @GET(Api.HOT_KEY)
    Observable<HotKeysData> getHotKeys();

    @GET(Api.BANNER)
    Observable<BannerData> getBanner();

    @POST(Api.COLLECT_ARTICLE+"{id}/json")
    Observable<Status> collectArticle(@Path("id") int id);

    @POST(Api.CANCEL_COLLECTING_ARTICLE + "{originId}/json")
    Observable<Status> uncollectArticle(@Path("originId") int originId);

    @GET(Api.GET_FAVORITE_ARTICLES + "{page}/json")
    Observable<FavoriteArticlesData> getFavoriteArticles(@Path("page") int page);



}
