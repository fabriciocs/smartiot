package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackResponseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeedbackResponse getFeedbackResponseSample1() {
        return new FeedbackResponse().id(1L).responseData("responseData1");
    }

    public static FeedbackResponse getFeedbackResponseSample2() {
        return new FeedbackResponse().id(2L).responseData("responseData2");
    }

    public static FeedbackResponse getFeedbackResponseRandomSampleGenerator() {
        return new FeedbackResponse().id(longCount.incrementAndGet()).responseData(UUID.randomUUID().toString());
    }
}
