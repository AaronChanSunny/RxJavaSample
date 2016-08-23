package me.aaron.rxjavasample;

import org.junit.Test;

import rx.observers.TestSubscriber;

/**
 * Created by Chenll on 2016/8/23 0023.
 */
public class RxSampleTest extends BaseTest {

    private RxSample mTest;

    @Override
    public void setup() {
        super.setup();

        mTest = new RxSample();
    }

    @Test
    public void testOnNext() throws Exception {
        TestSubscriber<String> expected = new TestSubscriber<>();

        mTest.onNext().subscribe(expected);

        expected.assertValues("1", "2");
        expected.assertCompleted();
    }

    @Test
    public void testJust() {
        TestSubscriber<String> expected = new TestSubscriber<>();

        mTest.just().subscribe(expected);

        expected.assertValues("Hello");
        expected.assertCompleted();
    }

    @Test
    public void testFrom() {
        TestSubscriber<String> expected = new TestSubscriber<>();

        mTest.from().subscribe(expected);

        expected.assertValues("1", "2", "3");
        expected.assertCompleted();
    }

    @Test
    public void testFilter() {
        TestSubscriber<String> expected = new TestSubscriber<>();

        mTest.filter().subscribe(expected);

        expected.assertValues("2", "2");
        expected.assertCompleted();
    }

}