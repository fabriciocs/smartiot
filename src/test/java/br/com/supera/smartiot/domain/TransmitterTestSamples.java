package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransmitterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Transmitter getTransmitterSample1() {
        return new Transmitter().id(1L).serialNumber("serialNumber1").frequency(1);
    }

    public static Transmitter getTransmitterSample2() {
        return new Transmitter().id(2L).serialNumber("serialNumber2").frequency(2);
    }

    public static Transmitter getTransmitterRandomSampleGenerator() {
        return new Transmitter()
            .id(longCount.incrementAndGet())
            .serialNumber(UUID.randomUUID().toString())
            .frequency(intCount.incrementAndGet());
    }
}
