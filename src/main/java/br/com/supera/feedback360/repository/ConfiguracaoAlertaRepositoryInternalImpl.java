package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.ConfiguracaoAlerta;
import br.com.supera.feedback360.repository.rowmapper.ConfiguracaoAlertaRowMapper;
import br.com.supera.feedback360.repository.rowmapper.SensorRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the ConfiguracaoAlerta entity.
 */
@SuppressWarnings("unused")
class ConfiguracaoAlertaRepositoryInternalImpl
    extends SimpleR2dbcRepository<ConfiguracaoAlerta, Long>
    implements ConfiguracaoAlertaRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SensorRowMapper sensorMapper;
    private final ConfiguracaoAlertaRowMapper configuracaoalertaMapper;

    private static final Table entityTable = Table.aliased("configuracao_alerta", EntityManager.ENTITY_ALIAS);
    private static final Table sensorTable = Table.aliased("sensor", "sensor");

    public ConfiguracaoAlertaRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SensorRowMapper sensorMapper,
        ConfiguracaoAlertaRowMapper configuracaoalertaMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(ConfiguracaoAlerta.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sensorMapper = sensorMapper;
        this.configuracaoalertaMapper = configuracaoalertaMapper;
    }

    @Override
    public Flux<ConfiguracaoAlerta> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<ConfiguracaoAlerta> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = ConfiguracaoAlertaSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SensorSqlHelper.getColumns(sensorTable, "sensor"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(sensorTable)
            .on(Column.create("sensor_id", entityTable))
            .equals(Column.create("id", sensorTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, ConfiguracaoAlerta.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<ConfiguracaoAlerta> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<ConfiguracaoAlerta> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<ConfiguracaoAlerta> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<ConfiguracaoAlerta> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<ConfiguracaoAlerta> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private ConfiguracaoAlerta process(Row row, RowMetadata metadata) {
        ConfiguracaoAlerta entity = configuracaoalertaMapper.apply(row, "e");
        entity.setSensor(sensorMapper.apply(row, "sensor"));
        return entity;
    }

    @Override
    public <S extends ConfiguracaoAlerta> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
