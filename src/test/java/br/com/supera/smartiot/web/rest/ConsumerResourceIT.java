package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.ConsumerAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.Consumer;
import br.com.supera.smartiot.repository.ConsumerRepository;
import br.com.supera.smartiot.service.dto.ConsumerDTO;
import br.com.supera.smartiot.service.mapper.ConsumerMapper;
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
 * Integration tests for the {@link ConsumerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsumerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_NEIGHBORHOOD = "AAAAAAAAAA";
    private static final String UPDATED_NEIGHBORHOOD = "BBBBBBBBBB";

    private static final Integer DEFAULT_PROPERTY_NUMBER = 1;
    private static final Integer UPDATED_PROPERTY_NUMBER = 2;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consumers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsumerMockMvc;

    private Consumer consumer;

    private Consumer insertedConsumer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumer createEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .name(DEFAULT_NAME)
            .street(DEFAULT_STREET)
            .neighborhood(DEFAULT_NEIGHBORHOOD)
            .propertyNumber(DEFAULT_PROPERTY_NUMBER)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL);
        return consumer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumer createUpdatedEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .name(UPDATED_NAME)
            .street(UPDATED_STREET)
            .neighborhood(UPDATED_NEIGHBORHOOD)
            .propertyNumber(UPDATED_PROPERTY_NUMBER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        return consumer;
    }

    @BeforeEach
    public void initTest() {
        consumer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedConsumer != null) {
            consumerRepository.delete(insertedConsumer);
            insertedConsumer = null;
        }
    }

    @Test
    @Transactional
    void createConsumer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);
        var returnedConsumerDTO = om.readValue(
            restConsumerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConsumerDTO.class
        );

        // Validate the Consumer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConsumer = consumerMapper.toEntity(returnedConsumerDTO);
        assertConsumerUpdatableFieldsEquals(returnedConsumer, getPersistedConsumer(returnedConsumer));

        insertedConsumer = returnedConsumer;
    }

    @Test
    @Transactional
    void createConsumerWithExistingId() throws Exception {
        // Create the Consumer with an existing ID
        consumer.setId(1L);
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consumer.setName(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStreetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consumer.setStreet(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNeighborhoodIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consumer.setNeighborhood(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPropertyNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consumer.setPropertyNumber(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consumer.setPhone(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consumer.setEmail(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsumers() throws Exception {
        // Initialize the database
        insertedConsumer = consumerRepository.saveAndFlush(consumer);

        // Get all the consumerList
        restConsumerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].neighborhood").value(hasItem(DEFAULT_NEIGHBORHOOD)))
            .andExpect(jsonPath("$.[*].propertyNumber").value(hasItem(DEFAULT_PROPERTY_NUMBER)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getConsumer() throws Exception {
        // Initialize the database
        insertedConsumer = consumerRepository.saveAndFlush(consumer);

        // Get the consumer
        restConsumerMockMvc
            .perform(get(ENTITY_API_URL_ID, consumer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consumer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.neighborhood").value(DEFAULT_NEIGHBORHOOD))
            .andExpect(jsonPath("$.propertyNumber").value(DEFAULT_PROPERTY_NUMBER))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingConsumer() throws Exception {
        // Get the consumer
        restConsumerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsumer() throws Exception {
        // Initialize the database
        insertedConsumer = consumerRepository.saveAndFlush(consumer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consumer
        Consumer updatedConsumer = consumerRepository.findById(consumer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsumer are not directly saved in db
        em.detach(updatedConsumer);
        updatedConsumer
            .name(UPDATED_NAME)
            .street(UPDATED_STREET)
            .neighborhood(UPDATED_NEIGHBORHOOD)
            .propertyNumber(UPDATED_PROPERTY_NUMBER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        ConsumerDTO consumerDTO = consumerMapper.toDto(updatedConsumer);

        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consumerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConsumerToMatchAllProperties(updatedConsumer);
    }

    @Test
    @Transactional
    void putNonExistingConsumer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumer.setId(longCount.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsumer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumer.setId(longCount.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsumer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumer.setId(longCount.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consumer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsumerWithPatch() throws Exception {
        // Initialize the database
        insertedConsumer = consumerRepository.saveAndFlush(consumer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consumer using partial update
        Consumer partialUpdatedConsumer = new Consumer();
        partialUpdatedConsumer.setId(consumer.getId());

        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsumer))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsumerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedConsumer, consumer), getPersistedConsumer(consumer));
    }

    @Test
    @Transactional
    void fullUpdateConsumerWithPatch() throws Exception {
        // Initialize the database
        insertedConsumer = consumerRepository.saveAndFlush(consumer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consumer using partial update
        Consumer partialUpdatedConsumer = new Consumer();
        partialUpdatedConsumer.setId(consumer.getId());

        partialUpdatedConsumer
            .name(UPDATED_NAME)
            .street(UPDATED_STREET)
            .neighborhood(UPDATED_NEIGHBORHOOD)
            .propertyNumber(UPDATED_PROPERTY_NUMBER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsumer))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsumerUpdatableFieldsEquals(partialUpdatedConsumer, getPersistedConsumer(partialUpdatedConsumer));
    }

    @Test
    @Transactional
    void patchNonExistingConsumer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumer.setId(longCount.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsumer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumer.setId(longCount.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsumer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumer.setId(longCount.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consumerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consumer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsumer() throws Exception {
        // Initialize the database
        insertedConsumer = consumerRepository.saveAndFlush(consumer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the consumer
        restConsumerMockMvc
            .perform(delete(ENTITY_API_URL_ID, consumer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return consumerRepository.count();
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

    protected Consumer getPersistedConsumer(Consumer consumer) {
        return consumerRepository.findById(consumer.getId()).orElseThrow();
    }

    protected void assertPersistedConsumerToMatchAllProperties(Consumer expectedConsumer) {
        assertConsumerAllPropertiesEquals(expectedConsumer, getPersistedConsumer(expectedConsumer));
    }

    protected void assertPersistedConsumerToMatchUpdatableProperties(Consumer expectedConsumer) {
        assertConsumerAllUpdatablePropertiesEquals(expectedConsumer, getPersistedConsumer(expectedConsumer));
    }
}
