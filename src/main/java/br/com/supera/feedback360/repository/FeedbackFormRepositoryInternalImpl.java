package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.FeedbackForm;
import br.com.supera.feedback360.repository.rowmapper.FeedbackFormRowMapper;
import br.com.supera.feedback360.repository.rowmapper.SysUserRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the FeedbackForm entity.
 */
@SuppressWarnings("unused")
class FeedbackFormRepositoryInternalImpl extends SimpleR2dbcRepository<FeedbackForm, Long> implements FeedbackFormRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SysUserRowMapper sysuserMapper;
    private final FeedbackFormRowMapper feedbackformMapper;

    private static final Table entityTable = Table.aliased("feedback_form", EntityManager.ENTITY_ALIAS);
    private static final Table creatorTable = Table.aliased("sys_user", "creator");

    public FeedbackFormRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SysUserRowMapper sysuserMapper,
        FeedbackFormRowMapper feedbackformMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FeedbackForm.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sysuserMapper = sysuserMapper;
        this.feedbackformMapper = feedbackformMapper;
    }

    @Override
    public Flux<FeedbackForm> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FeedbackForm> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FeedbackFormSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SysUserSqlHelper.getColumns(creatorTable, "creator"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(creatorTable)
            .on(Column.create("creator_id", entityTable))
            .equals(Column.create("id", creatorTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FeedbackForm.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FeedbackForm> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FeedbackForm> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<FeedbackForm> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<FeedbackForm> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<FeedbackForm> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private FeedbackForm process(Row row, RowMetadata metadata) {
        FeedbackForm entity = feedbackformMapper.apply(row, "e");
        entity.setCreator(sysuserMapper.apply(row, "creator"));
        return entity;
    }

    @Override
    public <S extends FeedbackForm> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
