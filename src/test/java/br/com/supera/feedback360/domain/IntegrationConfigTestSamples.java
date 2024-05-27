package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IntegrationConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IntegrationConfig getIntegrationConfigSample1() {
        return new IntegrationConfig().id(1L).serviceName("serviceName1").configData("configData1");
    }

    public static IntegrationConfig getIntegrationConfigSample2() {
        return new IntegrationConfig().id(2L).serviceName("serviceName2").configData("configData2");
    }

    public static IntegrationConfig getIntegrationConfigRandomSampleGenerator() {
        return new IntegrationConfig()
            .id(longCount.incrementAndGet())
            .serviceName(UUID.randomUUID().toString())
            .configData(UUID.randomUUID().toString());
    }
}
