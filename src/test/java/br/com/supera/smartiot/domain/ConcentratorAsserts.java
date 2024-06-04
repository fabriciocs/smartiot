package br.com.supera.smartiot.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcentratorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConcentratorAllPropertiesEquals(Concentrator expected, Concentrator actual) {
        assertConcentratorAutoGeneratedPropertiesEquals(expected, actual);
        assertConcentratorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConcentratorAllUpdatablePropertiesEquals(Concentrator expected, Concentrator actual) {
        assertConcentratorUpdatableFieldsEquals(expected, actual);
        assertConcentratorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConcentratorAutoGeneratedPropertiesEquals(Concentrator expected, Concentrator actual) {
        assertThat(expected)
            .as("Verify Concentrator auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConcentratorUpdatableFieldsEquals(Concentrator expected, Concentrator actual) {
        assertThat(expected)
            .as("Verify Concentrator relevant properties")
            .satisfies(e -> assertThat(e.getSerialNumber()).as("check serialNumber").isEqualTo(actual.getSerialNumber()))
            .satisfies(e -> assertThat(e.getCapacity()).as("check capacity").isEqualTo(actual.getCapacity()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConcentratorUpdatableRelationshipsEquals(Concentrator expected, Concentrator actual) {}
}