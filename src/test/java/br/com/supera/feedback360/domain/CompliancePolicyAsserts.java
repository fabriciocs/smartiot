package br.com.supera.feedback360.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CompliancePolicyAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompliancePolicyAllPropertiesEquals(CompliancePolicy expected, CompliancePolicy actual) {
        assertCompliancePolicyAutoGeneratedPropertiesEquals(expected, actual);
        assertCompliancePolicyAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompliancePolicyAllUpdatablePropertiesEquals(CompliancePolicy expected, CompliancePolicy actual) {
        assertCompliancePolicyUpdatableFieldsEquals(expected, actual);
        assertCompliancePolicyUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompliancePolicyAutoGeneratedPropertiesEquals(CompliancePolicy expected, CompliancePolicy actual) {
        assertThat(expected)
            .as("Verify CompliancePolicy auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompliancePolicyUpdatableFieldsEquals(CompliancePolicy expected, CompliancePolicy actual) {
        assertThat(expected)
            .as("Verify CompliancePolicy relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getRules()).as("check rules").isEqualTo(actual.getRules()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCompliancePolicyUpdatableRelationshipsEquals(CompliancePolicy expected, CompliancePolicy actual) {}
}
