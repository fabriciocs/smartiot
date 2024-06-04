package br.com.supera.smartiot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Consumer getConsumerSample1() {
        return new Consumer()
            .id(1L)
            .name("name1")
            .street("street1")
            .neighborhood("neighborhood1")
            .propertyNumber(1)
            .phone("phone1")
            .email("email1");
    }

    public static Consumer getConsumerSample2() {
        return new Consumer()
            .id(2L)
            .name("name2")
            .street("street2")
            .neighborhood("neighborhood2")
            .propertyNumber(2)
            .phone("phone2")
            .email("email2");
    }

    public static Consumer getConsumerRandomSampleGenerator() {
        return new Consumer()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .street(UUID.randomUUID().toString())
            .neighborhood(UUID.randomUUID().toString())
            .propertyNumber(intCount.incrementAndGet())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
