package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExternalSystemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExternalSystem getExternalSystemSample1() {
        return new ExternalSystem()
            .id(1L)
            .name("name1")
            .description("description1")
            .apiEndpoint("apiEndpoint1")
            .authDetails("authDetails1");
    }

    public static ExternalSystem getExternalSystemSample2() {
        return new ExternalSystem()
            .id(2L)
            .name("name2")
            .description("description2")
            .apiEndpoint("apiEndpoint2")
            .authDetails("authDetails2");
    }

    public static ExternalSystem getExternalSystemRandomSampleGenerator() {
        return new ExternalSystem()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .apiEndpoint(UUID.randomUUID().toString())
            .authDetails(UUID.randomUUID().toString());
    }
}
