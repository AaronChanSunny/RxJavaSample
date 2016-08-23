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

}