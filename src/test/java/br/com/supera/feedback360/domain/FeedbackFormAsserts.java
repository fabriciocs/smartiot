package br.com.supera.feedback360.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedbackFormAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackFormAllPropertiesEquals(FeedbackForm expected, FeedbackForm actual) {
        assertFeedbackFormAutoGeneratedPropertiesEquals(expected, actual);
        assertFeedbackFormAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackFormAllUpdatablePropertiesEquals(FeedbackForm expected, FeedbackForm actual) {
        assertFeedbackFormUpdatableFieldsEquals(expected, actual);
        assertFeedbackFormUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackFormAutoGeneratedPropertiesEquals(FeedbackForm expected, FeedbackForm actual) {
        assertThat(expected)
            .as("Verify FeedbackForm auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackFormUpdatableFieldsEquals(FeedbackForm expected, FeedbackForm actual) {
        assertThat(expected)
            .as("Verify FeedbackForm relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getCreatedAt()).as("check createdAt").isEqualTo(actual.getCreatedAt()))
            .satisfies(e -> assertThat(e.getUpdatedAt()).as("check updatedAt").isEqualTo(actual.getUpdatedAt()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackFormUpdatableRelationshipsEquals(FeedbackForm expected, FeedbackForm actual) {
        assertThat(expected)
            .as("Verify FeedbackForm relationships")
            .satisfies(e -> assertThat(e.getCreator()).as("check creator").isEqualTo(actual.getCreator()));
    }
}
