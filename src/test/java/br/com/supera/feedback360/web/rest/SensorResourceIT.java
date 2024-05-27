package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.SensorAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.Cliente;
import br.com.supera.feedback360.domain.Sensor;
import br.com.supera.feedback360.domain.enumeration.TipoSensor;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.SensorRepository;
import br.com.supera.feedback360.service.SensorService;
import br.com.supera.feedback360.service.dto.SensorDTO;
import br.com.supera.feedback360.service.mapper.SensorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link SensorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SensorResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final TipoSensor DEFAULT_TIPO = TipoSensor.TEMPERATURE;
    private static final TipoSensor UPDATED_TIPO = TipoSensor.HUMIDITY;

    private static final String DEFAULT_CONFIGURACAO = "AAAAAAAAAA";
    private static final String UPDATED_CONFIGURACAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sensors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SensorRepository sensorRepository;

    @Mock
    private SensorRepository sensorRepositoryMock;

    @Autowired
    private SensorMapper sensorMapper;

    @Mock
    private SensorService sensorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Sensor sensor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createEntity(EntityManager em) {
        Sensor sensor = new Sensor().nome(DEFAULT_NOME).tipo(DEFAULT_TIPO).configuracao(DEFAULT_CONFIGURACAO);
        // Add required entity
        Cliente cliente;
        cliente = em.insert(ClienteResourceIT.createEntity(em)).block();
        sensor.setCliente(cliente);
        return sensor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sensor createUpdatedEntity(EntityManager em) {
        Sensor sensor = new Sensor().nome(UPDATED_NOME).tipo(UPDATED_TIPO).configuracao(UPDATED_CONFIGURACAO);
        // Add required entity
        Cliente cliente;
        cliente = em.insert(ClienteResourceIT.createUpdatedEntity(em)).block();
        sensor.setCliente(cliente);
        return sensor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Sensor.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ClienteResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        sensor = createEntity(em);
    }

    @Test
    void createSensor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);
        var returnedSensorDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(SensorDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Sensor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSensor = sensorMapper.toEntity(returnedSensorDTO);
        assertSensorUpdatableFieldsEquals(returnedSensor, getPersistedSensor(returnedSensor));
    }

    @Test
    void createSensorWithExistingId() throws Exception {
        // Create the Sensor with an existing ID
        sensor.setId(1L);
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sensor.setNome(null);

        // Create the Sensor, which fails.
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sensor.setTipo(null);

        // Create the Sensor, which fails.
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSensors() {
        // Initialize the database
        sensorRepository.save(sensor).block();

        // Get all the sensorList
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
            .value(hasItem(sensor.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].tipo")
            .value(hasItem(DEFAULT_TIPO.toString()))
            .jsonPath("$.[*].configuracao")
            .value(hasItem(DEFAULT_CONFIGURACAO));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSensorsWithEagerRelationshipsIsEnabled() {
        when(sensorServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(sensorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSensorsWithEagerRelationshipsIsNotEnabled() {
        when(sensorServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(sensorRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSensor() {
        // Initialize the database
        sensorRepository.save(sensor).block();

        // Get the sensor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sensor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sensor.getId().intValue()))
            .jsonPath("$.nome")
            .value(is(DEFAULT_NOME))
            .jsonPath("$.tipo")
            .value(is(DEFAULT_TIPO.toString()))
            .jsonPath("$.configuracao")
            .value(is(DEFAULT_CONFIGURACAO));
    }

    @Test
    void getNonExistingSensor() {
        // Get the sensor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSensor() throws Exception {
        // Initialize the database
        sensorRepository.save(sensor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findById(sensor.getId()).block();
        updatedSensor.nome(UPDATED_NOME).tipo(UPDATED_TIPO).configuracao(UPDATED_CONFIGURACAO);
        SensorDTO sensorDTO = sensorMapper.toDto(updatedSensor);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sensorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSensorToMatchAllProperties(updatedSensor);
    }

    @Test
    void putNonExistingSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sensorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.save(sensor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSensorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSensor, sensor), getPersistedSensor(sensor));
    }

    @Test
    void fullUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.save(sensor).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor.nome(UPDATED_NOME).tipo(UPDATED_TIPO).configuracao(UPDATED_CONFIGURACAO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedSensor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSensorUpdatableFieldsEquals(partialUpdatedSensor, getPersistedSensor(partialUpdatedSensor));
    }

    @Test
    void patchNonExistingSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sensorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(sensorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSensor() {
        // Initialize the database
        sensorRepository.save(sensor).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sensor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sensor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sensorRepository.count().block();
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

    protected Sensor getPersistedSensor(Sensor sensor) {
        return sensorRepository.findById(sensor.getId()).block();
    }

    protected void assertPersistedSensorToMatchAllProperties(Sensor expectedSensor) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSensorAllPropertiesEquals(expectedSensor, getPersistedSensor(expectedSensor));
        assertSensorUpdatableFieldsEquals(expectedSensor, getPersistedSensor(expectedSensor));
    }

    protected void assertPersistedSensorToMatchUpdatableProperties(Sensor expectedSensor) {
        // Test fails because reactive api returns an empty object instead of null
        // assertSensorAllUpdatablePropertiesEquals(expectedSensor, getPersistedSensor(expectedSensor));
        assertSensorUpdatableFieldsEquals(expectedSensor, getPersistedSensor(expectedSensor));
    }
}
