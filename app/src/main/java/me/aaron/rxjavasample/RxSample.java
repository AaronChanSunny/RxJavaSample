package me.aaron.rxjavasample;

import rx.Observable;
import rx.Subscriber;

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

}
