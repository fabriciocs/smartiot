package br.com.supera.feedback360.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SensorAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSensorAllPropertiesEquals(Sensor expected, Sensor actual) {
        assertSensorAutoGeneratedPropertiesEquals(expected, actual);
        assertSensorAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSensorAllUpdatablePropertiesEquals(Sensor expected, Sensor actual) {
        assertSensorUpdatableFieldsEquals(expected, actual);
        assertSensorUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSensorAutoGeneratedPropertiesEquals(Sensor expected, Sensor actual) {
        assertThat(expected)
            .as("Verify Sensor auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSensorUpdatableFieldsEquals(Sensor expected, Sensor actual) {
        assertThat(expected)
            .as("Verify Sensor relevant properties")
            .satisfies(e -> assertThat(e.getNome()).as("check nome").isEqualTo(actual.getNome()))
            .satisfies(e -> assertThat(e.getTipo()).as("check tipo").isEqualTo(actual.getTipo()))
            .satisfies(e -> assertThat(e.getConfiguracao()).as("check configuracao").isEqualTo(actual.getConfiguracao()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSensorUpdatableRelationshipsEquals(Sensor expected, Sensor actual) {
        assertThat(expected)
            .as("Verify Sensor relationships")
            .satisfies(e -> assertThat(e.getCliente()).as("check cliente").isEqualTo(actual.getCliente()))
            .satisfies(e -> assertThat(e.getDadoSensores()).as("check dadoSensores").isEqualTo(actual.getDadoSensores()));
    }
}
