package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackFormTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeedbackForm getFeedbackFormSample1() {
        return new FeedbackForm().id(1L).title("title1").description("description1").status("status1");
    }

    public static FeedbackForm getFeedbackFormSample2() {
        return new FeedbackForm().id(2L).title("title2").description("description2").status("status2");
    }

    public static FeedbackForm getFeedbackFormRandomSampleGenerator() {
        return new FeedbackForm()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
