package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Profile getProfileSample1() {
        return new Profile()
            .id(1L)
            .phoneNumber("phoneNumber1")
            .address("address1")
            .profilePicture("profilePicture1")
            .preferences("preferences1");
    }

    public static Profile getProfileSample2() {
        return new Profile()
            .id(2L)
            .phoneNumber("phoneNumber2")
            .address("address2")
            .profilePicture("profilePicture2")
            .preferences("preferences2");
    }

    public static Profile getProfileRandomSampleGenerator() {
        return new Profile()
            .id(longCount.incrementAndGet())
            .phoneNumber(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .profilePicture(UUID.randomUUID().toString())
            .preferences(UUID.randomUUID().toString());
    }
}
