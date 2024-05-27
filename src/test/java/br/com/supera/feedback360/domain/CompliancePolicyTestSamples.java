package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompliancePolicyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CompliancePolicy getCompliancePolicySample1() {
        return new CompliancePolicy().id(1L).name("name1").description("description1").rules("rules1");
    }

    public static CompliancePolicy getCompliancePolicySample2() {
        return new CompliancePolicy().id(2L).name("name2").description("description2").rules("rules2");
    }

    public static CompliancePolicy getCompliancePolicyRandomSampleGenerator() {
        return new CompliancePolicy()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .rules(UUID.randomUUID().toString());
    }
}
