package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PricingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pricing getPricingSample1() {
        return new Pricing().id(1L).serviceType("serviceType1");
    }

    public static Pricing getPricingSample2() {
        return new Pricing().id(2L).serviceType("serviceType2");
    }

    public static Pricing getPricingRandomSampleGenerator() {
        return new Pricing().id(longCount.incrementAndGet()).serviceType(UUID.randomUUID().toString());
    }
}
