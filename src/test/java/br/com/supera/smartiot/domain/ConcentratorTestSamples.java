package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConcentratorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Concentrator getConcentratorSample1() {
        return new Concentrator().id(1L).serialNumber("serialNumber1").capacity(1);
    }

    public static Concentrator getConcentratorSample2() {
        return new Concentrator().id(2L).serialNumber("serialNumber2").capacity(2);
    }

    public static Concentrator getConcentratorRandomSampleGenerator() {
        return new Concentrator()
            .id(longCount.incrementAndGet())
            .serialNumber(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet());
    }
}
