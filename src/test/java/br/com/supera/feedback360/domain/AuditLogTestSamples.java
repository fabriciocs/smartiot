package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuditLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AuditLog getAuditLogSample1() {
        return new AuditLog().id(1L).action("action1").details("details1");
    }

    public static AuditLog getAuditLogSample2() {
        return new AuditLog().id(2L).action("action2").details("details2");
    }

    public static AuditLog getAuditLogRandomSampleGenerator() {
        return new AuditLog().id(longCount.incrementAndGet()).action(UUID.randomUUID().toString()).details(UUID.randomUUID().toString());
    }
}
