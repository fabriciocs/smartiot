package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.FeedbackResponse;
import br.com.supera.feedback360.repository.rowmapper.FeedbackFormRowMapper;
import br.com.supera.feedback360.repository.rowmapper.FeedbackResponseRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the FeedbackResponse entity.
 */
@SuppressWarnings("unused")
class FeedbackResponseRepositoryInternalImpl
    extends SimpleR2dbcRepository<FeedbackResponse, Long>
    implements FeedbackResponseRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FeedbackFormRowMapper feedbackformMapper;
    private final SysUserRowMapper sysuserMapper;
    private final FeedbackResponseRowMapper feedbackresponseMapper;

    private static final Table entityTable = Table.aliased("feedback_response", EntityManager.ENTITY_ALIAS);
    private static final Table formTable = Table.aliased("feedback_form", "form");
    private static final Table userTable = Table.aliased("sys_user", "e_user");

    public FeedbackResponseRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FeedbackFormRowMapper feedbackformMapper,
        SysUserRowMapper sysuserMapper,
        FeedbackResponseRowMapper feedbackresponseMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FeedbackResponse.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.feedbackformMapper = feedbackformMapper;
        this.sysuserMapper = sysuserMapper;
        this.feedbackresponseMapper = feedbackresponseMapper;
    }

    @Override
    public Flux<FeedbackResponse> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FeedbackResponse> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FeedbackResponseSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FeedbackFormSqlHelper.getColumns(formTable, "form"));
        columns.addAll(SysUserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(formTable)
            .on(Column.create("form_id", entityTable))
            .equals(Column.create("id", formTable))
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FeedbackResponse.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FeedbackResponse> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FeedbackResponse> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<FeedbackResponse> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<FeedbackResponse> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<FeedbackResponse> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private FeedbackResponse process(Row row, RowMetadata metadata) {
        FeedbackResponse entity = feedbackresponseMapper.apply(row, "e");
        entity.setForm(feedbackformMapper.apply(row, "form"));
        entity.setUser(sysuserMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends FeedbackResponse> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
