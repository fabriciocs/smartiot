package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ManualEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ManualEntry getManualEntrySample1() {
        return new ManualEntry().id(1L).entryType("entryType1").value("value1");
    }

    public static ManualEntry getManualEntrySample2() {
        return new ManualEntry().id(2L).entryType("entryType2").value("value2");
    }

    public static ManualEntry getManualEntryRandomSampleGenerator() {
        return new ManualEntry()
            .id(longCount.incrementAndGet())
            .entryType(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString());
    }
}
