package com.example.lengary_l.wanandroid.retrofit;

import com.example.lengary_l.wanandroid.data.CategoryData;
import com.example.lengary_l.wanandroid.data.ArticlesData;
import com.example.lengary_l.wanandroid.data.LoginData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Observable<CategoryData> getCategories();

}
