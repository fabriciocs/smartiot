package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.TransmitterAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Transmitter;
import br.com.supera.smartiot.repository.TransmitterRepository;
import br.com.supera.smartiot.service.dto.TransmitterDTO;
import br.com.supera.smartiot.service.mapper.TransmitterMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TransmitterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransmitterResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_FREQUENCY = 1;
    private static final Integer UPDATED_FREQUENCY = 2;

    private static final String ENTITY_API_URL = "/api/transmitters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransmitterRepository transmitterRepository;

    @Autowired
    private TransmitterMapper transmitterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransmitterMockMvc;

    private Transmitter transmitter;

    private Transmitter insertedTransmitter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transmitter createEntity(EntityManager em) {
        Transmitter transmitter = new Transmitter().serialNumber(DEFAULT_SERIAL_NUMBER).frequency(DEFAULT_FREQUENCY);
        return transmitter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transmitter createUpdatedEntity(EntityManager em) {
        Transmitter transmitter = new Transmitter().serialNumber(UPDATED_SERIAL_NUMBER).frequency(UPDATED_FREQUENCY);
        return transmitter;
    }

    @BeforeEach
    public void initTest() {
        transmitter = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTransmitter != null) {
            transmitterRepository.delete(insertedTransmitter);
            insertedTransmitter = null;
        }
    }

    @Test
    @Transactional
    void createTransmitter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Transmitter
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);
        var returnedTransmitterDTO = om.readValue(
            restTransmitterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transmitterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransmitterDTO.class
        );

        // Validate the Transmitter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransmitter = transmitterMapper.toEntity(returnedTransmitterDTO);
        assertTransmitterUpdatableFieldsEquals(returnedTransmitter, getPersistedTransmitter(returnedTransmitter));

        insertedTransmitter = returnedTransmitter;
    }

    @Test
    @Transactional
    void createTransmitterWithExistingId() throws Exception {
        // Create the Transmitter with an existing ID
        transmitter.setId(1L);
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransmitterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transmitterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transmitter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transmitter.setSerialNumber(null);

        // Create the Transmitter, which fails.
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        restTransmitterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transmitterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFrequencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transmitter.setFrequency(null);

        // Create the Transmitter, which fails.
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        restTransmitterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transmitterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransmitters() throws Exception {
        // Initialize the database
        insertedTransmitter = transmitterRepository.saveAndFlush(transmitter);

        // Get all the transmitterList
        restTransmitterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transmitter.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)));
    }

    @Test
    @Transactional
    void getTransmitter() throws Exception {
        // Initialize the database
        insertedTransmitter = transmitterRepository.saveAndFlush(transmitter);

        // Get the transmitter
        restTransmitterMockMvc
            .perform(get(ENTITY_API_URL_ID, transmitter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transmitter.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY));
    }

    @Test
    @Transactional
    void getNonExistingTransmitter() throws Exception {
        // Get the transmitter
        restTransmitterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransmitter() throws Exception {
        // Initialize the database
        insertedTransmitter = transmitterRepository.saveAndFlush(transmitter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transmitter
        Transmitter updatedTransmitter = transmitterRepository.findById(transmitter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransmitter are not directly saved in db
        em.detach(updatedTransmitter);
        updatedTransmitter.serialNumber(UPDATED_SERIAL_NUMBER).frequency(UPDATED_FREQUENCY);
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(updatedTransmitter);

        restTransmitterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transmitterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transmitterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transmitter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransmitterToMatchAllProperties(updatedTransmitter);
    }

    @Test
    @Transactional
    void putNonExistingTransmitter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transmitter.setId(longCount.incrementAndGet());

        // Create the Transmitter
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransmitterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transmitterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transmitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transmitter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransmitter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transmitter.setId(longCount.incrementAndGet());

        // Create the Transmitter
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransmitterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transmitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transmitter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransmitter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transmitter.setId(longCount.incrementAndGet());

        // Create the Transmitter
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransmitterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transmitterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transmitter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransmitterWithPatch() throws Exception {
        // Initialize the database
        insertedTransmitter = transmitterRepository.saveAndFlush(transmitter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transmitter using partial update
        Transmitter partialUpdatedTransmitter = new Transmitter();
        partialUpdatedTransmitter.setId(transmitter.getId());

        partialUpdatedTransmitter.serialNumber(UPDATED_SERIAL_NUMBER);

        restTransmitterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransmitter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransmitter))
            )
            .andExpect(status().isOk());

        // Validate the Transmitter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransmitterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransmitter, transmitter),
            getPersistedTransmitter(transmitter)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransmitterWithPatch() throws Exception {
        // Initialize the database
        insertedTransmitter = transmitterRepository.saveAndFlush(transmitter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transmitter using partial update
        Transmitter partialUpdatedTransmitter = new Transmitter();
        partialUpdatedTransmitter.setId(transmitter.getId());

        partialUpdatedTransmitter.serialNumber(UPDATED_SERIAL_NUMBER).frequency(UPDATED_FREQUENCY);

        restTransmitterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransmitter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransmitter))
            )
            .andExpect(status().isOk());

        // Validate the Transmitter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransmitterUpdatableFieldsEquals(partialUpdatedTransmitter, getPersistedTransmitter(partialUpdatedTransmitter));
    }

    @Test
    @Transactional
    void patchNonExistingTransmitter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transmitter.setId(longCount.incrementAndGet());

        // Create the Transmitter
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransmitterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transmitterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transmitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transmitter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransmitter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transmitter.setId(longCount.incrementAndGet());

        // Create the Transmitter
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransmitterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transmitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transmitter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransmitter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transmitter.setId(longCount.incrementAndGet());

        // Create the Transmitter
        TransmitterDTO transmitterDTO = transmitterMapper.toDto(transmitter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransmitterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transmitterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transmitter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransmitter() throws Exception {
        // Initialize the database
        insertedTransmitter = transmitterRepository.saveAndFlush(transmitter);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transmitter
        restTransmitterMockMvc
            .perform(delete(ENTITY_API_URL_ID, transmitter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transmitterRepository.count();
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

    protected Transmitter getPersistedTransmitter(Transmitter transmitter) {
        return transmitterRepository.findById(transmitter.getId()).orElseThrow();
    }

    protected void assertPersistedTransmitterToMatchAllProperties(Transmitter expectedTransmitter) {
        assertTransmitterAllPropertiesEquals(expectedTransmitter, getPersistedTransmitter(expectedTransmitter));
    }

    protected void assertPersistedTransmitterToMatchUpdatableProperties(Transmitter expectedTransmitter) {
        assertTransmitterAllUpdatablePropertiesEquals(expectedTransmitter, getPersistedTransmitter(expectedTransmitter));
    }
}
