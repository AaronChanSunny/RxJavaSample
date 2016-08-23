# RxJavaSample

## 简介

用简短的几句话来介绍 RxJava？
- 异步
- 观察者模式
- 链式编程
- 事件流

具体来说，RxJava 为我们提供给了哪些能力？
- 极大简化嵌套异步请求
在传统的 Java 编程中，异步操作的结果是通过回调接口通知给调用者。当一个异步操作需要依赖另一个异步操作的结果时，就会产生
回调嵌套。具体到代码上，就会有大量的缩进代码或者跳转。
- 线程的切换变得非常简单
- 统一的错误处理
Java并不支持从子线程中抛出异常，我们只能在子线程中捕获异常并进行处理。而如果使用Rx，不管错误发生在哪个事件节点，它
最终都能把错误抛给观察者。
- 数据拼装和变化能力

## 配置

`app/build.gradle:`：
 ```
 dependencies {
    compile 'io.reactivex:rxandroid:1.2.0'
    compile 'io.reactivex:rxjava:1.1.4'
  }
 ```

## 开始

![](http://ww3.sinaimg.cn/mw1024/52eb2279jw1f2rx46dspqj20gn04qaad.jpg)

- 创建观察者

Rx中观察者是一个接口 `Observable`，定义了 `onNext()`、`onCompleted()`、`onError()` 三个方法。最基本的创建方式：
```
Observer<String> observer = new Observer<String>() {
    @Override
    public void onNext(String s) {
        Log.d(tag, "Item: " + s);
    }

    @Override
    public void onCompleted() {
        Log.d(tag, "Completed!");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(tag, "Error!");
    }
};
```

除了基本创建方式外，Rx库还实现了一个 `Observer` 抽象类：`Subscriber`，使用方式和 `Observable` 是一样的：
```
Subscriber<String> subscriber = new Subscriber<String>() {
    @Override
    public void onNext(String s) {
        Log.d(tag, "Item: " + s);
    }

    @Override
    public void onCompleted() {
        Log.d(tag, "Completed!");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(tag, "Error!");
    }
};
```

- 创建被观察者

`Observable` 即被观察者，它决定什么时候触发事件以及触发怎样的事件。`Observable` 标准的创建方式：

```
Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
    @Override
    public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext("Hello");
        subscriber.onNext("Hi");
        subscriber.onNext("ND");
        subscriber.onCompleted();
    }
});
```

除了标准的创建方式之外，Rx还提供了额外的快捷方法来创建 `Observable`：`just()`、`from`。这些统一在操作符章节介绍。

- Make it work

有了被观察者和观察者，接下来就可以让它们建立关系，让整个事件流动起来。具体的代码形式很简单：

```
observable.subscribe(observer);
// 或者：
observable.subscribe(subscriber);
```

关键的方法在 `subscribe`，可以跳转到相应源码：

```
static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
    ......
    try {
        // allow the hook to intercept and/or decorate
        hook.onSubscribeStart(observable, observable.onSubscribe).call(subscriber);
        return hook.onSubscribeReturn(subscriber);
    } catch (Throwable e) {
    ......
    }
}
```

因此，`Observable` 并不是在创建的时候是不会发送任何事件的，只有当它被订阅的时候，即 `subscribe` 方法执行的时候，才开始
整个事件流。

## 操作符

### 创建

- `just`

创建一个 `Observable`，发送 `just` 中的数据项。

![](http://reactivex.io/documentation/operators/images/just.c.png)

```
public Observable<String> just() {
    return Observable.just("Hello");
}
```

```
@Test
public void testJust() {
    TestSubscriber<String> expected = new TestSubscriber<>();
    mTest.just().subscribe(expected);
    expected.assertValues("Hello");
    expected.assertCompleted();
}
```

- `from`

将非 `Observable` 数据类型转换为 `Observable`。

![](http://reactivex.io/documentation/operators/images/from.c.png)

```
public Observable<String> from() {
    return Observable.from(new String[]{
            "1", "2", "3"
    });
}
```

```
@Test
public void testFrom() {
    TestSubscriber<String> expected = new TestSubscriber<>();
    mTest.from().subscribe(expected);
    expected.assertValues("1", "2", "3");
    expected.assertCompleted();
}
```

### 转换

- `map`

将一个 `Observable` 的数据项转换成另外一种格式。这边的转换可以是简单的数学函数，也可以是不同的数据结构。

![](https://raw.github.com/wiki/ReactiveX/RxJava/images/rx-operators/map.png)

```
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
```

```
@Test
public void testMap() {
    TestSubscriber<Integer> expected = new TestSubscriber<>();
    mTest.map().subscribe(expected);
    expected.assertValue(1);
    expected.onCompleted();
}
```

- `flatMap`

接收一个 `Observable` 作为输入，同时输出一个新的 `Observable`。这个操作符常用于链式异步请求，例如：用户登录时首先
会请求服务端获得 `token`，取到 `token` 后再执行登录操作。

![](http://reactivex.io/documentation/operators/images/flatMap.c.png)

```
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
    return Observable.just(token + " login");
}
private Observable<String> getToken(long uid) {
    return Observable.just(uid + " token");
}
```

```
@Test
public void testFlatMap() {
    TestSubscriber<String> expected = new TestSubscriber<>();
    mTest.flatMap().subscribe(expected);
    expected.assertValues("291212 token login");
    expected.onCompleted();
}
```

- `lift`

Rx所有的变换都是基于操作符 `lift`。简单地说，在 `Observable` 执行了 `lift(Operator)` 方法之后，会返回一个
新的 `Observable`，这个新的 `Observable` 会像一个代理一样，负责接收原始的 `Observable` 发出的事件，并在处理后发送
给 `Subscriber`。
`lift` 的过程可以理解成一种代理机制，通过事件拦截和处理实现事件序列的变换。

![](https://camo.githubusercontent.com/c39371130e099fe4dc3eda01a26f6180a2f8c69f/687474703a2f2f7777312e73696e61696d672e636e2f6d77313032342f35326562323237396a77316632727863726e6132376a323068343064317134662e6a7067)

```
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
```

```
@Test
public void testLift() {
    TestSubscriber<String> expected = new TestSubscriber<>();
    mTest.lift().subscribe(expected);
    expected.assertValues("291212");
    expected.onCompleted();
}
```

### 过滤

- `filter`

仅发送符合过滤条件的数据项。

![](https://raw.githubusercontent.com/wiki/ReactiveX/RxJava/images/rx-operators/filter.png)

```
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
```

```
@Test
public void testFilter() {
    TestSubscriber<String> expected = new TestSubscriber<>();
    mTest.filter().subscribe(expected);
    expected.assertValues("2", "2");
    expected.assertCompleted();
}
```

## 线程切换

## 参考

- [Reactivex Documentation](http://reactivex.io/documentation/operators.html)
- [给 Android 开发者的 RxJava 详解](http://gank.io/post/560e15be2dca930e00da1083#toc_17)