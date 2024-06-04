package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AggregatedDataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AggregatedData getAggregatedDataSample1() {
        return new AggregatedData().id(1L).dataType("dataType1").value("value1");
    }

    public static AggregatedData getAggregatedDataSample2() {
        return new AggregatedData().id(2L).dataType("dataType2").value("value2");
    }

    public static AggregatedData getAggregatedDataRandomSampleGenerator() {
        return new AggregatedData()
            .id(longCount.incrementAndGet())
            .dataType(UUID.randomUUID().toString())
            .value(UUID.randomUUID().toString());
    }
}
