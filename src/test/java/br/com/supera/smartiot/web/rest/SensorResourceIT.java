package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.SensorAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Cliente;
import br.com.supera.smartiot.domain.Sensor;
import br.com.supera.smartiot.domain.enumeration.TipoSensor;
import br.com.supera.smartiot.repository.SensorRepository;
import br.com.supera.smartiot.service.SensorService;
import br.com.supera.smartiot.service.dto.SensorDTO;
import br.com.supera.smartiot.service.mapper.SensorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SensorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
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
    private MockMvc restSensorMockMvc;

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
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
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
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        sensor.setCliente(cliente);
        return sensor;
    }

    @BeforeEach
    public void initTest() {
        sensor = createEntity(em);
    }

    @Test
    @Transactional
    void createSensor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);
        var returnedSensorDTO = om.readValue(
            restSensorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sensorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SensorDTO.class
        );

        // Validate the Sensor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSensor = sensorMapper.toEntity(returnedSensorDTO);
        assertSensorUpdatableFieldsEquals(returnedSensor, getPersistedSensor(returnedSensor));
    }

    @Test
    @Transactional
    void createSensorWithExistingId() throws Exception {
        // Create the Sensor with an existing ID
        sensor.setId(1L);
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sensorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sensor.setNome(null);

        // Create the Sensor, which fails.
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        restSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sensorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sensor.setTipo(null);

        // Create the Sensor, which fails.
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        restSensorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sensorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList
        restSensorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].configuracao").value(hasItem(DEFAULT_CONFIGURACAO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSensorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sensorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSensorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sensorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSensorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sensorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSensorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sensorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get the sensor
        restSensorMockMvc
            .perform(get(ENTITY_API_URL_ID, sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sensor.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.configuracao").value(DEFAULT_CONFIGURACAO));
    }

    @Test
    @Transactional
    void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findById(sensor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSensor are not directly saved in db
        em.detach(updatedSensor);
        updatedSensor.nome(UPDATED_NOME).tipo(UPDATED_TIPO).configuracao(UPDATED_CONFIGURACAO);
        SensorDTO sensorDTO = sensorMapper.toDto(updatedSensor);

        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sensorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSensorToMatchAllProperties(updatedSensor);
    }

    @Test
    @Transactional
    void putNonExistingSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sensorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor.configuracao(UPDATED_CONFIGURACAO);

        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSensor))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSensorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSensor, sensor), getPersistedSensor(sensor));
    }

    @Test
    @Transactional
    void fullUpdateSensorWithPatch() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sensor using partial update
        Sensor partialUpdatedSensor = new Sensor();
        partialUpdatedSensor.setId(sensor.getId());

        partialUpdatedSensor.nome(UPDATED_NOME).tipo(UPDATED_TIPO).configuracao(UPDATED_CONFIGURACAO);

        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSensor))
            )
            .andExpect(status().isOk());

        // Validate the Sensor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSensorUpdatableFieldsEquals(partialUpdatedSensor, getPersistedSensor(partialUpdatedSensor));
    }

    @Test
    @Transactional
    void patchNonExistingSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sensorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sensorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSensor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sensor.setId(longCount.incrementAndGet());

        // Create the Sensor
        SensorDTO sensorDTO = sensorMapper.toDto(sensor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sensorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sensor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sensor
        restSensorMockMvc
            .perform(delete(ENTITY_API_URL_ID, sensor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sensorRepository.count();
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
        return sensorRepository.findById(sensor.getId()).orElseThrow();
    }

    protected void assertPersistedSensorToMatchAllProperties(Sensor expectedSensor) {
        assertSensorAllPropertiesEquals(expectedSensor, getPersistedSensor(expectedSensor));
    }

    protected void assertPersistedSensorToMatchUpdatableProperties(Sensor expectedSensor) {
        assertSensorAllUpdatablePropertiesEquals(expectedSensor, getPersistedSensor(expectedSensor));
    }
}
