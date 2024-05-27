package br.com.supera.feedback360.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportAllPropertiesEquals(Report expected, Report actual) {
        assertReportAutoGeneratedPropertiesEquals(expected, actual);
        assertReportAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportAllUpdatablePropertiesEquals(Report expected, Report actual) {
        assertReportUpdatableFieldsEquals(expected, actual);
        assertReportUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportAutoGeneratedPropertiesEquals(Report expected, Report actual) {
        assertThat(expected)
            .as("Verify Report auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportUpdatableFieldsEquals(Report expected, Report actual) {
        assertThat(expected)
            .as("Verify Report relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getGeneratedAt()).as("check generatedAt").isEqualTo(actual.getGeneratedAt()))
            .satisfies(e -> assertThat(e.getContent()).as("check content").isEqualTo(actual.getContent()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReportUpdatableRelationshipsEquals(Report expected, Report actual) {
        assertThat(expected)
            .as("Verify Report relationships")
            .satisfies(e -> assertThat(e.getGeneratedBy()).as("check generatedBy").isEqualTo(actual.getGeneratedBy()));
    }
}