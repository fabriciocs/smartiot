package br.com.supera.feedback360.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalyticsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyticsAllPropertiesEquals(Analytics expected, Analytics actual) {
        assertAnalyticsAutoGeneratedPropertiesEquals(expected, actual);
        assertAnalyticsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyticsAllUpdatablePropertiesEquals(Analytics expected, Analytics actual) {
        assertAnalyticsUpdatableFieldsEquals(expected, actual);
        assertAnalyticsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyticsAutoGeneratedPropertiesEquals(Analytics expected, Analytics actual) {
        assertThat(expected)
            .as("Verify Analytics auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyticsUpdatableFieldsEquals(Analytics expected, Analytics actual) {
        assertThat(expected)
            .as("Verify Analytics relevant properties")
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getData()).as("check data").isEqualTo(actual.getData()))
            .satisfies(e -> assertThat(e.getCreatedAt()).as("check createdAt").isEqualTo(actual.getCreatedAt()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyticsUpdatableRelationshipsEquals(Analytics expected, Analytics actual) {}
}