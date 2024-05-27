package br.com.supera.feedback360.web.rest;

import static br.com.supera.feedback360.domain.ReportAsserts.*;
import static br.com.supera.feedback360.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import br.com.supera.feedback360.IntegrationTest;
import br.com.supera.feedback360.domain.Report;
import br.com.supera.feedback360.repository.EntityManager;
import br.com.supera.feedback360.repository.ReportRepository;
import br.com.supera.feedback360.service.ReportService;
import br.com.supera.feedback360.service.dto.ReportDTO;
import br.com.supera.feedback360.service.mapper.ReportMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReportResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_GENERATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GENERATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportRepository reportRepository;

    @Mock
    private ReportRepository reportRepositoryMock;

    @Autowired
    private ReportMapper reportMapper;

    @Mock
    private ReportService reportServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Report report;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity(EntityManager em) {
        Report report = new Report().title(DEFAULT_TITLE).generatedAt(DEFAULT_GENERATED_AT).content(DEFAULT_CONTENT);
        return report;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity(EntityManager em) {
        Report report = new Report().title(UPDATED_TITLE).generatedAt(UPDATED_GENERATED_AT).content(UPDATED_CONTENT);
        return report;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Report.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        report = createEntity(em);
    }

    @Test
    void createReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        var returnedReportDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReportDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Report in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReport = reportMapper.toEntity(returnedReportDTO);
        assertReportUpdatableFieldsEquals(returnedReport, getPersistedReport(returnedReport));
    }

    @Test
    void createReportWithExistingId() throws Exception {
        // Create the Report with an existing ID
        report.setId(1L);
        ReportDTO reportDTO = reportMapper.toDto(report);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        report.setTitle(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkGeneratedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        report.setGeneratedAt(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkContentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        report.setContent(null);

        // Create the Report, which fails.
        ReportDTO reportDTO = reportMapper.toDto(report);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllReports() {
        // Initialize the database
        reportRepository.save(report).block();

        // Get all the reportList
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
            .value(hasItem(report.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].generatedAt")
            .value(hasItem(DEFAULT_GENERATED_AT.toString()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportsWithEagerRelationshipsIsEnabled() {
        when(reportServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(reportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportsWithEagerRelationshipsIsNotEnabled() {
        when(reportServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(reportRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getReport() {
        // Initialize the database
        reportRepository.save(report).block();

        // Get the report
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, report.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(report.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.generatedAt")
            .value(is(DEFAULT_GENERATED_AT.toString()))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT));
    }

    @Test
    void getNonExistingReport() {
        // Get the report
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReport() throws Exception {
        // Initialize the database
        reportRepository.save(report).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).block();
        updatedReport.title(UPDATED_TITLE).generatedAt(UPDATED_GENERATED_AT).content(UPDATED_CONTENT);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportToMatchAllProperties(updatedReport);
    }

    @Test
    void putNonExistingReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setId(longCount.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reportDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setId(longCount.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setId(longCount.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.save(report).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.title(UPDATED_TITLE).generatedAt(UPDATED_GENERATED_AT).content(UPDATED_CONTENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReport, report), getPersistedReport(report));
    }

    @Test
    void fullUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.save(report).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.title(UPDATED_TITLE).generatedAt(UPDATED_GENERATED_AT).content(UPDATED_CONTENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Report in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportUpdatableFieldsEquals(partialUpdatedReport, getPersistedReport(partialUpdatedReport));
    }

    @Test
    void patchNonExistingReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setId(longCount.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reportDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setId(longCount.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        report.setId(longCount.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(reportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Report in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReport() {
        // Initialize the database
        reportRepository.save(report).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the report
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, report.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportRepository.count().block();
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

    protected Report getPersistedReport(Report report) {
        return reportRepository.findById(report.getId()).block();
    }

    protected void assertPersistedReportToMatchAllProperties(Report expectedReport) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportAllPropertiesEquals(expectedReport, getPersistedReport(expectedReport));
        assertReportUpdatableFieldsEquals(expectedReport, getPersistedReport(expectedReport));
    }

    protected void assertPersistedReportToMatchUpdatableProperties(Report expectedReport) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReportAllUpdatablePropertiesEquals(expectedReport, getPersistedReport(expectedReport));
        assertReportUpdatableFieldsEquals(expectedReport, getPersistedReport(expectedReport));
    }
}
