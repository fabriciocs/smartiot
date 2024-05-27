package br.com.supera.feedback360.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ExternalSystemAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertExternalSystemAllPropertiesEquals(ExternalSystem expected, ExternalSystem actual) {
        assertExternalSystemAutoGeneratedPropertiesEquals(expected, actual);
        assertExternalSystemAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertExternalSystemAllUpdatablePropertiesEquals(ExternalSystem expected, ExternalSystem actual) {
        assertExternalSystemUpdatableFieldsEquals(expected, actual);
        assertExternalSystemUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertExternalSystemAutoGeneratedPropertiesEquals(ExternalSystem expected, ExternalSystem actual) {
        assertThat(expected)
            .as("Verify ExternalSystem auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertExternalSystemUpdatableFieldsEquals(ExternalSystem expected, ExternalSystem actual) {
        assertThat(expected)
            .as("Verify ExternalSystem relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getApiEndpoint()).as("check apiEndpoint").isEqualTo(actual.getApiEndpoint()))
            .satisfies(e -> assertThat(e.getAuthDetails()).as("check authDetails").isEqualTo(actual.getAuthDetails()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertExternalSystemUpdatableRelationshipsEquals(ExternalSystem expected, ExternalSystem actual) {}
}
