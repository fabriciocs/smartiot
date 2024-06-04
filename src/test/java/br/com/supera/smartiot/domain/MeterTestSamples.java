package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MeterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Meter getMeterSample1() {
        return new Meter().id(1L).serialNumber("serialNumber1").location("location1");
    }

    public static Meter getMeterSample2() {
        return new Meter().id(2L).serialNumber("serialNumber2").location("location2");
    }

    public static Meter getMeterRandomSampleGenerator() {
        return new Meter()
            .id(longCount.incrementAndGet())
            .serialNumber(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString());
    }
}
