package br.com.supera.feedback360.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedbackResponseAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackResponseAllPropertiesEquals(FeedbackResponse expected, FeedbackResponse actual) {
        assertFeedbackResponseAutoGeneratedPropertiesEquals(expected, actual);
        assertFeedbackResponseAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackResponseAllUpdatablePropertiesEquals(FeedbackResponse expected, FeedbackResponse actual) {
        assertFeedbackResponseUpdatableFieldsEquals(expected, actual);
        assertFeedbackResponseUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackResponseAutoGeneratedPropertiesEquals(FeedbackResponse expected, FeedbackResponse actual) {
        assertThat(expected)
            .as("Verify FeedbackResponse auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackResponseUpdatableFieldsEquals(FeedbackResponse expected, FeedbackResponse actual) {
        assertThat(expected)
            .as("Verify FeedbackResponse relevant properties")
            .satisfies(e -> assertThat(e.getResponseData()).as("check responseData").isEqualTo(actual.getResponseData()))
            .satisfies(e -> assertThat(e.getSubmittedAt()).as("check submittedAt").isEqualTo(actual.getSubmittedAt()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFeedbackResponseUpdatableRelationshipsEquals(FeedbackResponse expected, FeedbackResponse actual) {
        assertThat(expected)
            .as("Verify FeedbackResponse relationships")
            .satisfies(e -> assertThat(e.getForm()).as("check form").isEqualTo(actual.getForm()))
            .satisfies(e -> assertThat(e.getUser()).as("check user").isEqualTo(actual.getUser()));
    }
}
