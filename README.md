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

[](http://ww3.sinaimg.cn/mw1024/52eb2279jw1f2rx46dspqj20gn04qaad.jpg)

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

## 线程切换

## 参考

- []()