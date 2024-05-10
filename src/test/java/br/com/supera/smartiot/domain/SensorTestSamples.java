package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SensorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Sensor getSensorSample1() {
        return new Sensor().id(1L).nome("nome1").configuracao("configuracao1");
    }

    public static Sensor getSensorSample2() {
        return new Sensor().id(2L).nome("nome2").configuracao("configuracao2");
    }

    public static Sensor getSensorRandomSampleGenerator() {
        return new Sensor().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).configuracao(UUID.randomUUID().toString());
    }
}
