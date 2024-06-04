package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class ManualEntryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManualEntryAllPropertiesEquals(ManualEntry expected, ManualEntry actual) {
        assertManualEntryAutoGeneratedPropertiesEquals(expected, actual);
        assertManualEntryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManualEntryAllUpdatablePropertiesEquals(ManualEntry expected, ManualEntry actual) {
        assertManualEntryUpdatableFieldsEquals(expected, actual);
        assertManualEntryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManualEntryAutoGeneratedPropertiesEquals(ManualEntry expected, ManualEntry actual) {
        assertThat(expected)
            .as("Verify ManualEntry auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManualEntryUpdatableFieldsEquals(ManualEntry expected, ManualEntry actual) {
        assertThat(expected)
            .as("Verify ManualEntry relevant properties")
            .satisfies(e -> assertThat(e.getEntryType()).as("check entryType").isEqualTo(actual.getEntryType()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()))
            .satisfies(
                e ->
                    assertThat(e.getEntryDate())
                        .as("check entryDate")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getEntryDate())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertManualEntryUpdatableRelationshipsEquals(ManualEntry expected, ManualEntry actual) {}
}
