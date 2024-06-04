package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Report getReportSample1() {
        return new Report().id(1L).reportName("reportName1").reportData("reportData1");
    }

    public static Report getReportSample2() {
        return new Report().id(2L).reportName("reportName2").reportData("reportData2");
    }

    public static Report getReportRandomSampleGenerator() {
        return new Report()
            .id(longCount.incrementAndGet())
            .reportName(UUID.randomUUID().toString())
            .reportData(UUID.randomUUID().toString());
    }
}
