package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DadoSensorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DadoSensor getDadoSensorSample1() {
        return new DadoSensor().id(1L).dados("dados1");
    }

    public static DadoSensor getDadoSensorSample2() {
        return new DadoSensor().id(2L).dados("dados2");
    }

    public static DadoSensor getDadoSensorRandomSampleGenerator() {
        return new DadoSensor().id(longCount.incrementAndGet()).dados(UUID.randomUUID().toString());
    }
}
