package com.example.lengary_l.wanandroid.RxBus;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by CoderLengary
 */


public class RxBus {
    public static final String REFRESH = "REFRESH";
    private static volatile RxBus INSTANCE;
    private final Subject<Object> subject = PublishSubject.create().toSerialized();
    private Disposable disposable;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RxBus() {

    }

    public static RxBus getInstance() {
        if (INSTANCE == null) {
            synchronized (RxBus.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxBus();
                }
            }
        }
        return INSTANCE;
    }

    public void send(Object object) {
        subject.onNext(object);
    }

    public <T>Observable<T> tObservable(Class<T> classType){
        return subject.ofType(classType);
    }

    public void subscribe(Class bean, Consumer consumer) {
        disposable = tObservable(bean).subscribe(consumer);
        compositeDisposable.add(disposable);
    }

    public void unSubscribe(){
        compositeDisposable.clear();

    }
}
