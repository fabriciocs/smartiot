package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.DadoSensorAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.DadoSensor;
import br.com.supera.feedback360.repository.DadoSensorRepository;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.service.dto.DadoSensorDTO;
import br.com.supera.feedback360.service.mapper.DadoSensorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link DadoSensorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DadoSensorResourceIT {

    private static final String DEFAULT_DADOS = "AAAAAAAAAA";
    private static final String UPDATED_DADOS = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/dado-sensors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DadoSensorRepository dadoSensorRepository;

    @Autowired
    private DadoSensorMapper dadoSensorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DadoSensor dadoSensor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DadoSensor createEntity(EntityManager em) {
        DadoSensor dadoSensor = new DadoSensor().dados(DEFAULT_DADOS).timestamp(DEFAULT_TIMESTAMP);
        return dadoSensor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DadoSensor createUpdatedEntity(EntityManager em) {
        DadoSensor dadoSensor = new DadoSensor().dados(UPDATED_DADOS).timestamp(UPDATED_TIMESTAMP);
        return dadoSensor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DadoSensor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        SensorResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        dadoSensor = createEntity(em);
    }

    @Test
    void createDadoSensor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);
        var returnedDadoSensorDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(DadoSensorDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the DadoSensor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDadoSensor = dadoSensorMapper.toEntity(returnedDadoSensorDTO);
        assertDadoSensorUpdatableFieldsEquals(returnedDadoSensor, getPersistedDadoSensor(returnedDadoSensor));
    }

    @Test
    void createDadoSensorWithExistingId() throws Exception {
        // Create the DadoSensor with an existing ID
        dadoSensor.setId(1L);
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDadoSensors() {
        // Initialize the database
        dadoSensorRepository.save(dadoSensor).block();

        // Get all the dadoSensorList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(dadoSensor.getId().intValue()))
            .jsonPath("$.[*].dados")
            .value(hasItem(DEFAULT_DADOS))
            .jsonPath("$.[*].timestamp")
            .value(hasItem(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    void getDadoSensor() {
        // Initialize the database
        dadoSensorRepository.save(dadoSensor).block();

        // Get the dadoSensor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, dadoSensor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(dadoSensor.getId().intValue()))
            .jsonPath("$.dados")
            .value(is(DEFAULT_DADOS))
            .jsonPath("$.timestamp")
            .value(is(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    void getNonExistingDadoSensor() {
        // Get the dadoSensor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDadoSensor() throws Exception {
        // Initialize the database
        dadoSensorRepository.save(dadoSensor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dadoSensor
        DadoSensor updatedDadoSensor = dadoSensorRepository.findById(dadoSensor.getId()).block();
        updatedDadoSensor.dados(UPDATED_DADOS).timestamp(UPDATED_TIMESTAMP);
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(updatedDadoSensor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dadoSensorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDadoSensorToMatchAllProperties(updatedDadoSensor);
    }

    @Test
    void putNonExistingDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dadoSensorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDadoSensorWithPatch() throws Exception {
        // Initialize the database
        dadoSensorRepository.save(dadoSensor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dadoSensor using partial update
        DadoSensor partialUpdatedDadoSensor = new DadoSensor();
        partialUpdatedDadoSensor.setId(dadoSensor.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDadoSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDadoSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DadoSensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDadoSensorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDadoSensor, dadoSensor),
            getPersistedDadoSensor(dadoSensor)
        );
    }

    @Test
    void fullUpdateDadoSensorWithPatch() throws Exception {
        // Initialize the database
        dadoSensorRepository.save(dadoSensor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dadoSensor using partial update
        DadoSensor partialUpdatedDadoSensor = new DadoSensor();
        partialUpdatedDadoSensor.setId(dadoSensor.getId());

        partialUpdatedDadoSensor.dados(UPDATED_DADOS).timestamp(UPDATED_TIMESTAMP);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDadoSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDadoSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DadoSensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDadoSensorUpdatableFieldsEquals(partialUpdatedDadoSensor, getPersistedDadoSensor(partialUpdatedDadoSensor));
    }

    @Test
    void patchNonExistingDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, dadoSensorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDadoSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dadoSensor.setId(longCount.incrementAndGet());

        // Create the DadoSensor
        DadoSensorDTO dadoSensorDTO = dadoSensorMapper.toDto(dadoSensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(dadoSensorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DadoSensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDadoSensor() {
        // Initialize the database
        dadoSensorRepository.save(dadoSensor).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dadoSensor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, dadoSensor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dadoSensorRepository.count().block();
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

    protected DadoSensor getPersistedDadoSensor(DadoSensor dadoSensor) {
        return dadoSensorRepository.findById(dadoSensor.getId()).block();
    }

    protected void assertPersistedDadoSensorToMatchAllProperties(DadoSensor expectedDadoSensor) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDadoSensorAllPropertiesEquals(expectedDadoSensor, getPersistedDadoSensor(expectedDadoSensor));
        assertDadoSensorUpdatableFieldsEquals(expectedDadoSensor, getPersistedDadoSensor(expectedDadoSensor));
    }

    protected void assertPersistedDadoSensorToMatchUpdatableProperties(DadoSensor expectedDadoSensor) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDadoSensorAllUpdatablePropertiesEquals(expectedDadoSensor, getPersistedDadoSensor(expectedDadoSensor));
        assertDadoSensorUpdatableFieldsEquals(expectedDadoSensor, getPersistedDadoSensor(expectedDadoSensor));
    }
}
