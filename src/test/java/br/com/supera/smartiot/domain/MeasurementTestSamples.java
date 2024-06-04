package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MeasurementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Measurement getMeasurementSample1() {
        return new Measurement().id(1L).measurementType("measurementType1").value("value1");
    }

    public static Measurement getMeasurementSample2() {
        return new Measurement().id(2L).measurementType("measurementType2").value("value2");
    }

    public static Measurement getMeasurementRandomSampleGenerator() {
        return new Measurement()
            .id(longCount.incrementAndGet())
            .measurementType(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString());
    }
}
