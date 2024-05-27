package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.ConfiguracaoAlertaAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static br.com.supera.feedback360.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.ConfiguracaoAlerta;
import br.com.supera.feedback360.domain.Sensor;
import br.com.supera.feedback360.repository.ConfiguracaoAlertaRepository;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.service.ConfiguracaoAlertaService;
import br.com.supera.feedback360.service.dto.ConfiguracaoAlertaDTO;
import br.com.supera.feedback360.service.mapper.ConfiguracaoAlertaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ConfiguracaoAlertaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ConfiguracaoAlertaResourceIT {

    private static final BigDecimal DEFAULT_LIMITE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIMITE = new BigDecimal(2);

    private static final String DEFAULT_EMAIL = "::I_bA@8aLB.hC4Dcf";
    private static final String UPDATED_EMAIL = "$:yn@Py1.9`";

    private static final String ENTITY_API_URL = "/api/configuracao-alertas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConfiguracaoAlertaRepository configuracaoAlertaRepository;

    @Mock
    private ConfiguracaoAlertaRepository configuracaoAlertaRepositoryMock;

    @Autowired
    private ConfiguracaoAlertaMapper configuracaoAlertaMapper;

    @Mock
    private ConfiguracaoAlertaService configuracaoAlertaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ConfiguracaoAlerta configuracaoAlerta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfiguracaoAlerta createEntity(EntityManager em) {
        ConfiguracaoAlerta configuracaoAlerta = new ConfiguracaoAlerta().limite(DEFAULT_LIMITE).email(DEFAULT_EMAIL);
        // Add required entity
        Sensor sensor;
        sensor = em.insert(SensorResourceIT.createEntity(em)).block();
        configuracaoAlerta.setSensor(sensor);
        return configuracaoAlerta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfiguracaoAlerta createUpdatedEntity(EntityManager em) {
        ConfiguracaoAlerta configuracaoAlerta = new ConfiguracaoAlerta().limite(UPDATED_LIMITE).email(UPDATED_EMAIL);
        // Add required entity
        Sensor sensor;
        sensor = em.insert(SensorResourceIT.createUpdatedEntity(em)).block();
        configuracaoAlerta.setSensor(sensor);
        return configuracaoAlerta;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ConfiguracaoAlerta.class).block();
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
        configuracaoAlerta = createEntity(em);
    }

    @Test
    void createConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);
        var returnedConfiguracaoAlertaDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ConfiguracaoAlertaDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the ConfiguracaoAlerta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConfiguracaoAlerta = configuracaoAlertaMapper.toEntity(returnedConfiguracaoAlertaDTO);
        assertConfiguracaoAlertaUpdatableFieldsEquals(
            returnedConfiguracaoAlerta,
            getPersistedConfiguracaoAlerta(returnedConfiguracaoAlerta)
        );
    }

    @Test
    void createConfiguracaoAlertaWithExistingId() throws Exception {
        // Create the ConfiguracaoAlerta with an existing ID
        configuracaoAlerta.setId(1L);
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configuracaoAlerta.setEmail(null);

        // Create the ConfiguracaoAlerta, which fails.
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllConfiguracaoAlertas() {
        // Initialize the database
        configuracaoAlertaRepository.save(configuracaoAlerta).block();

        // Get all the configuracaoAlertaList
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
            .value(hasItem(configuracaoAlerta.getId().intValue()))
            .jsonPath("$.[*].limite")
            .value(hasItem(sameNumber(DEFAULT_LIMITE)))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConfiguracaoAlertasWithEagerRelationshipsIsEnabled() {
        when(configuracaoAlertaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(configuracaoAlertaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConfiguracaoAlertasWithEagerRelationshipsIsNotEnabled() {
        when(configuracaoAlertaServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(configuracaoAlertaRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getConfiguracaoAlerta() {
        // Initialize the database
        configuracaoAlertaRepository.save(configuracaoAlerta).block();

        // Get the configuracaoAlerta
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, configuracaoAlerta.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(configuracaoAlerta.getId().intValue()))
            .jsonPath("$.limite")
            .value(is(sameNumber(DEFAULT_LIMITE)))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL));
    }

    @Test
    void getNonExistingConfiguracaoAlerta() {
        // Get the configuracaoAlerta
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingConfiguracaoAlerta() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.save(configuracaoAlerta).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoAlerta
        ConfiguracaoAlerta updatedConfiguracaoAlerta = configuracaoAlertaRepository.findById(configuracaoAlerta.getId()).block();
        updatedConfiguracaoAlerta.limite(UPDATED_LIMITE).email(UPDATED_EMAIL);
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(updatedConfiguracaoAlerta);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, configuracaoAlertaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConfiguracaoAlertaToMatchAllProperties(updatedConfiguracaoAlerta);
    }

    @Test
    void putNonExistingConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, configuracaoAlertaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateConfiguracaoAlertaWithPatch() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.save(configuracaoAlerta).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoAlerta using partial update
        ConfiguracaoAlerta partialUpdatedConfiguracaoAlerta = new ConfiguracaoAlerta();
        partialUpdatedConfiguracaoAlerta.setId(configuracaoAlerta.getId());

        partialUpdatedConfiguracaoAlerta.email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedConfiguracaoAlerta.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedConfiguracaoAlerta))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ConfiguracaoAlerta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfiguracaoAlertaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConfiguracaoAlerta, configuracaoAlerta),
            getPersistedConfiguracaoAlerta(configuracaoAlerta)
        );
    }

    @Test
    void fullUpdateConfiguracaoAlertaWithPatch() throws Exception {
        // Initialize the database
        configuracaoAlertaRepository.save(configuracaoAlerta).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoAlerta using partial update
        ConfiguracaoAlerta partialUpdatedConfiguracaoAlerta = new ConfiguracaoAlerta();
        partialUpdatedConfiguracaoAlerta.setId(configuracaoAlerta.getId());

        partialUpdatedConfiguracaoAlerta.limite(UPDATED_LIMITE).email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedConfiguracaoAlerta.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedConfiguracaoAlerta))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ConfiguracaoAlerta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfiguracaoAlertaUpdatableFieldsEquals(
            partialUpdatedConfiguracaoAlerta,
            getPersistedConfiguracaoAlerta(partialUpdatedConfiguracaoAlerta)
        );
    }

    @Test
    void patchNonExistingConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, configuracaoAlertaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamConfiguracaoAlerta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoAlerta.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoAlerta
        ConfiguracaoAlertaDTO configuracaoAlertaDTO = configuracaoAlertaMapper.toDto(configuracaoAlerta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(configuracaoAlertaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ConfiguracaoAlerta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteConfiguracaoAlerta() {
        // Initialize the database
        configuracaoAlertaRepository.save(configuracaoAlerta).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the configuracaoAlerta
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, configuracaoAlerta.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return configuracaoAlertaRepository.count().block();
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

    protected ConfiguracaoAlerta getPersistedConfiguracaoAlerta(ConfiguracaoAlerta configuracaoAlerta) {
        return configuracaoAlertaRepository.findById(configuracaoAlerta.getId()).block();
    }

    protected void assertPersistedConfiguracaoAlertaToMatchAllProperties(ConfiguracaoAlerta expectedConfiguracaoAlerta) {
        // Test fails because reactive api returns an empty object instead of null
        // assertConfiguracaoAlertaAllPropertiesEquals(expectedConfiguracaoAlerta, getPersistedConfiguracaoAlerta(expectedConfiguracaoAlerta));
        assertConfiguracaoAlertaUpdatableFieldsEquals(
            expectedConfiguracaoAlerta,
            getPersistedConfiguracaoAlerta(expectedConfiguracaoAlerta)
        );
    }

    protected void assertPersistedConfiguracaoAlertaToMatchUpdatableProperties(ConfiguracaoAlerta expectedConfiguracaoAlerta) {
        // Test fails because reactive api returns an empty object instead of null
        // assertConfiguracaoAlertaAllUpdatablePropertiesEquals(expectedConfiguracaoAlerta, getPersistedConfiguracaoAlerta(expectedConfiguracaoAlerta));
        assertConfiguracaoAlertaUpdatableFieldsEquals(
            expectedConfiguracaoAlerta,
            getPersistedConfiguracaoAlerta(expectedConfiguracaoAlerta)
        );
    }
}
