package br.com.supera.feedback360.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationSettingsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationSettingsAllPropertiesEquals(NotificationSettings expected, NotificationSettings actual) {
        assertNotificationSettingsAutoGeneratedPropertiesEquals(expected, actual);
        assertNotificationSettingsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationSettingsAllUpdatablePropertiesEquals(NotificationSettings expected, NotificationSettings actual) {
        assertNotificationSettingsUpdatableFieldsEquals(expected, actual);
        assertNotificationSettingsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationSettingsAutoGeneratedPropertiesEquals(NotificationSettings expected, NotificationSettings actual) {
        assertThat(expected)
            .as("Verify NotificationSettings auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationSettingsUpdatableFieldsEquals(NotificationSettings expected, NotificationSettings actual) {
        assertThat(expected)
            .as("Verify NotificationSettings relevant properties")
            .satisfies(e -> assertThat(e.getPreferences()).as("check preferences").isEqualTo(actual.getPreferences()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertNotificationSettingsUpdatableRelationshipsEquals(NotificationSettings expected, NotificationSettings actual) {
        assertThat(expected)
            .as("Verify NotificationSettings relationships")
            .satisfies(e -> assertThat(e.getUser()).as("check user").isEqualTo(actual.getUser()));
    }
}
