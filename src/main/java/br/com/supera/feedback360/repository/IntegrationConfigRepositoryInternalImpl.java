package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.IntegrationConfig;
import br.com.supera.feedback360.repository.rowmapper.ExternalSystemRowMapper;
import br.com.supera.feedback360.repository.rowmapper.IntegrationConfigRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the IntegrationConfig entity.
 */
@SuppressWarnings("unused")
class IntegrationConfigRepositoryInternalImpl
    extends SimpleR2dbcRepository<IntegrationConfig, Long>
    implements IntegrationConfigRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ExternalSystemRowMapper externalsystemMapper;
    private final IntegrationConfigRowMapper integrationconfigMapper;

    private static final Table entityTable = Table.aliased("integration_config", EntityManager.ENTITY_ALIAS);
    private static final Table externalSystemTable = Table.aliased("external_system", "externalSystem");

    public IntegrationConfigRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ExternalSystemRowMapper externalsystemMapper,
        IntegrationConfigRowMapper integrationconfigMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(IntegrationConfig.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.externalsystemMapper = externalsystemMapper;
        this.integrationconfigMapper = integrationconfigMapper;
    }

    @Override
    public Flux<IntegrationConfig> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<IntegrationConfig> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = IntegrationConfigSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ExternalSystemSqlHelper.getColumns(externalSystemTable, "externalSystem"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(externalSystemTable)
            .on(Column.create("external_system_id", entityTable))
            .equals(Column.create("id", externalSystemTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, IntegrationConfig.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<IntegrationConfig> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<IntegrationConfig> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<IntegrationConfig> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<IntegrationConfig> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<IntegrationConfig> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private IntegrationConfig process(Row row, RowMetadata metadata) {
        IntegrationConfig entity = integrationconfigMapper.apply(row, "e");
        entity.setExternalSystem(externalsystemMapper.apply(row, "externalSystem"));
        return entity;
    }

    @Override
    public <S extends IntegrationConfig> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
