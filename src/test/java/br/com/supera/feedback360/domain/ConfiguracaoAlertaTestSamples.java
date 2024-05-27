package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConfiguracaoAlertaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConfiguracaoAlerta getConfiguracaoAlertaSample1() {
        return new ConfiguracaoAlerta().id(1L).email("email1");
    }

    public static ConfiguracaoAlerta getConfiguracaoAlertaSample2() {
        return new ConfiguracaoAlerta().id(2L).email("email2");
    }

    public static ConfiguracaoAlerta getConfiguracaoAlertaRandomSampleGenerator() {
        return new ConfiguracaoAlerta().id(longCount.incrementAndGet()).email(UUID.randomUUID().toString());
    }
}
