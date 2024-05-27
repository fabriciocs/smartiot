package br.com.supera.feedback360.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NotificationSettingsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static NotificationSettings getNotificationSettingsSample1() {
        return new NotificationSettings().id(1L).preferences("preferences1");
    }

    public static NotificationSettings getNotificationSettingsSample2() {
        return new NotificationSettings().id(2L).preferences("preferences2");
    }

    public static NotificationSettings getNotificationSettingsRandomSampleGenerator() {
        return new NotificationSettings().id(longCount.incrementAndGet()).preferences(UUID.randomUUID().toString());
    }
}
