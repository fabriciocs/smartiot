package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.EnrollmentAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.smartiot.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Enrollment;
import br.com.supera.smartiot.repository.EnrollmentRepository;
import br.com.supera.smartiot.service.dto.EnrollmentDTO;
import br.com.supera.smartiot.service.mapper.EnrollmentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EnrollmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnrollmentResourceIT {

    private static final String DEFAULT_ENROLLMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENROLLMENT_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_ENROLLMENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENROLLMENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/enrollments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnrollmentMockMvc;

    private Enrollment enrollment;

    private Enrollment insertedEnrollment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enrollment createEntity(EntityManager em) {
        Enrollment enrollment = new Enrollment().enrollmentType(DEFAULT_ENROLLMENT_TYPE).enrollmentDate(DEFAULT_ENROLLMENT_DATE);
        return enrollment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enrollment createUpdatedEntity(EntityManager em) {
        Enrollment enrollment = new Enrollment().enrollmentType(UPDATED_ENROLLMENT_TYPE).enrollmentDate(UPDATED_ENROLLMENT_DATE);
        return enrollment;
    }

    @BeforeEach
    public void initTest() {
        enrollment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEnrollment != null) {
            enrollmentRepository.delete(insertedEnrollment);
            insertedEnrollment = null;
        }
    }

    @Test
    @Transactional
    void createEnrollment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);
        var returnedEnrollmentDTO = om.readValue(
            restEnrollmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnrollmentDTO.class
        );

        // Validate the Enrollment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnrollment = enrollmentMapper.toEntity(returnedEnrollmentDTO);
        assertEnrollmentUpdatableFieldsEquals(returnedEnrollment, getPersistedEnrollment(returnedEnrollment));

        insertedEnrollment = returnedEnrollment;
    }

    @Test
    @Transactional
    void createEnrollmentWithExistingId() throws Exception {
        // Create the Enrollment with an existing ID
        enrollment.setId(1L);
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnrollmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnrollmentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enrollment.setEnrollmentType(null);

        // Create the Enrollment, which fails.
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        restEnrollmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnrollmentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enrollment.setEnrollmentDate(null);

        // Create the Enrollment, which fails.
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        restEnrollmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnrollments() throws Exception {
        // Initialize the database
        insertedEnrollment = enrollmentRepository.saveAndFlush(enrollment);

        // Get all the enrollmentList
        restEnrollmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enrollment.getId().intValue())))
            .andExpect(jsonPath("$.[*].enrollmentType").value(hasItem(DEFAULT_ENROLLMENT_TYPE)))
            .andExpect(jsonPath("$.[*].enrollmentDate").value(hasItem(sameInstant(DEFAULT_ENROLLMENT_DATE))));
    }

    @Test
    @Transactional
    void getEnrollment() throws Exception {
        // Initialize the database
        insertedEnrollment = enrollmentRepository.saveAndFlush(enrollment);

        // Get the enrollment
        restEnrollmentMockMvc
            .perform(get(ENTITY_API_URL_ID, enrollment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enrollment.getId().intValue()))
            .andExpect(jsonPath("$.enrollmentType").value(DEFAULT_ENROLLMENT_TYPE))
            .andExpect(jsonPath("$.enrollmentDate").value(sameInstant(DEFAULT_ENROLLMENT_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingEnrollment() throws Exception {
        // Get the enrollment
        restEnrollmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnrollment() throws Exception {
        // Initialize the database
        insertedEnrollment = enrollmentRepository.saveAndFlush(enrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enrollment
        Enrollment updatedEnrollment = enrollmentRepository.findById(enrollment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnrollment are not directly saved in db
        em.detach(updatedEnrollment);
        updatedEnrollment.enrollmentType(UPDATED_ENROLLMENT_TYPE).enrollmentDate(UPDATED_ENROLLMENT_DATE);
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(updatedEnrollment);

        restEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enrollmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnrollmentToMatchAllProperties(updatedEnrollment);
    }

    @Test
    @Transactional
    void putNonExistingEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enrollmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnrollmentWithPatch() throws Exception {
        // Initialize the database
        insertedEnrollment = enrollmentRepository.saveAndFlush(enrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enrollment using partial update
        Enrollment partialUpdatedEnrollment = new Enrollment();
        partialUpdatedEnrollment.setId(enrollment.getId());

        partialUpdatedEnrollment.enrollmentType(UPDATED_ENROLLMENT_TYPE);

        restEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the Enrollment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnrollmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnrollment, enrollment),
            getPersistedEnrollment(enrollment)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnrollmentWithPatch() throws Exception {
        // Initialize the database
        insertedEnrollment = enrollmentRepository.saveAndFlush(enrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enrollment using partial update
        Enrollment partialUpdatedEnrollment = new Enrollment();
        partialUpdatedEnrollment.setId(enrollment.getId());

        partialUpdatedEnrollment.enrollmentType(UPDATED_ENROLLMENT_TYPE).enrollmentDate(UPDATED_ENROLLMENT_DATE);

        restEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the Enrollment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnrollmentUpdatableFieldsEquals(partialUpdatedEnrollment, getPersistedEnrollment(partialUpdatedEnrollment));
    }

    @Test
    @Transactional
    void patchNonExistingEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enrollmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnrollment() throws Exception {
        // Initialize the database
        insertedEnrollment = enrollmentRepository.saveAndFlush(enrollment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enrollment
        restEnrollmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, enrollment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enrollmentRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Enrollment getPersistedEnrollment(Enrollment enrollment) {
        return enrollmentRepository.findById(enrollment.getId()).orElseThrow();
    }

    protected void assertPersistedEnrollmentToMatchAllProperties(Enrollment expectedEnrollment) {
        assertEnrollmentAllPropertiesEquals(expectedEnrollment, getPersistedEnrollment(expectedEnrollment));
    }

    protected void assertPersistedEnrollmentToMatchUpdatableProperties(Enrollment expectedEnrollment) {
        assertEnrollmentAllUpdatablePropertiesEquals(expectedEnrollment, getPersistedEnrollment(expectedEnrollment));
    }
}
