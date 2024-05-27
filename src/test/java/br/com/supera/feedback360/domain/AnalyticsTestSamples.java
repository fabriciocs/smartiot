package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnalyticsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Analytics getAnalyticsSample1() {
        return new Analytics().id(1L).type("type1").data("data1");
    }

    public static Analytics getAnalyticsSample2() {
        return new Analytics().id(2L).type("type2").data("data2");
    }

    public static Analytics getAnalyticsRandomSampleGenerator() {
        return new Analytics().id(longCount.incrementAndGet()).type(UUID.randomUUID().toString()).data(UUID.randomUUID().toString());
    }
}
