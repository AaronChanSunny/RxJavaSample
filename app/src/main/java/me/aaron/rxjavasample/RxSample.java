package me.aaron.rxjavasample;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Chenll on 2016/8/23 0023.
 */
public class RxSample {

    public Observable<String> onNext() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onCompleted();
                subscriber.onNext("3");
            }
        });
    }

    public Observable<String> just() {
        return Observable.just("Hello");
    }

    public Observable<String> from() {
        return Observable.from(new String[]{
                "1", "2", "3"
        });
    }

    public Observable<String> filter() {
        return Observable
                .from(new String[]{
                        "1", "2", "3", "2"
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return "2".equalsIgnoreCase(s);
                    }
                });
    }

}
