package com.example.lengary_l.wanandroid.mvp.detail;

import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.StatusDataRepository;

import io.reactivex.disposables.CompositeDisposable;

public class DetailPresenter implements DetailContract.Presenter{
    private DetailContract.View view;
    private StatusDataRepository statusDataRepository;
    private ArticlesDataRepository articlesDataRepository;
    private CompositeDisposable compositeDisposable;


    public DetailPresenter(DetailContract.View view,
                           StatusDataRepository statusDataRepository,
                           ArticlesDataRepository articlesDataRepository) {
        this.view = view;
        this.statusDataRepository = statusDataRepository;
        this.articlesDataRepository = articlesDataRepository;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void collectArticle(int id) {
        statusDataRepository.collectArticle(id);
    }

    @Override
    public void uncollectArticle(int originId) {
        statusDataRepository.uncollectArticle(originId);
    }

    @Override
    public void addToReadLater(int id, int userId) {

    }
}
