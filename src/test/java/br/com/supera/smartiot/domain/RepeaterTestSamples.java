package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RepeaterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Repeater getRepeaterSample1() {
        return new Repeater().id(1L).serialNumber("serialNumber1").range(1);
    }

    public static Repeater getRepeaterSample2() {
        return new Repeater().id(2L).serialNumber("serialNumber2").range(2);
    }

    public static Repeater getRepeaterRandomSampleGenerator() {
        return new Repeater().id(longCount.incrementAndGet()).serialNumber(UUID.randomUUID().toString()).range(intCount.incrementAndGet());
    }
}
