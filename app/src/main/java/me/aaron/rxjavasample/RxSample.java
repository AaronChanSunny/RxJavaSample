package me.aaron.rxjavasample;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Chenll on 2016/8/23 0023.
 */
public class RxSample {

    public Observable<String> onNext() {
        return Observable
                .create(new Observable.OnSubscribe<String>() {
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
        return Observable
                .just("Hello");
    }

    public Observable<String> from() {
        return Observable
                .from(new String[]{
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

    public Observable<Integer> map() {
        return Observable
                .just("1")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return Integer.valueOf(s);
                    }
                });
    }

    public Observable<String> flatMap() {
        return Observable
                .just(291212L)
                .flatMap(new Func1<Long, Observable<String>>() {
                    @Override
                    public Observable<String> call(Long uid) {
                        return getToken(291212L);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String token) {
                        return login(token);
                    }
                });
    }

    private Observable<String> login(String token) {
        return Observable
                .just(token + " login");
    }

    private Observable<String> getToken(long uid) {
        return Observable
                .just(uid + " token");
    }

    public Observable<String> lift() {
        return Observable
                .just(291212L)
                .lift(new Observable.Operator<String, Long>() {
                    @Override
                    public Subscriber<? super Long> call(final Subscriber<? super String> subscriber) {
                        return new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(Long aLong) {
                                subscriber.onNext("" + aLong);
                            }
                        };
                    }
                });
    }

    public Observable<Long> merge() {
        Observable<Long> observable1 = Observable
                .interval(0, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 5;
                    }
                })
                .take(5);

        Observable<Long> observable2 = Observable
                .interval(500, 1000, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong * 10;
                    }
                })
                .take(5);

        return Observable
                .merge(observable1, observable2);
    }

    public Observable<String> take() {
        return Observable
                .just("1", "2", "3", "4", "5")
                .take(3);
    }

    public Observable<String> toList() {
        Observable<String> observable1 = Observable
                .just("1", "2", "3");

        Observable<String> observable2 = Observable
                .just("4", "5");

        return Observable
                .merge(observable1, observable2)
                .toList()
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<String> items) {
                        String result = "";
                        for (String item : items) {
                            result += item + "";
                        }

                        return Observable.just(result);
                    }
                });
    }

}
