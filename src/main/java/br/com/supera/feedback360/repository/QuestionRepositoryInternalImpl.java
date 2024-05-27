package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.Question;
import br.com.supera.feedback360.repository.rowmapper.FeedbackFormRowMapper;
import br.com.supera.feedback360.repository.rowmapper.QuestionRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Question entity.
 */
@SuppressWarnings("unused")
class QuestionRepositoryInternalImpl extends SimpleR2dbcRepository<Question, Long> implements QuestionRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FeedbackFormRowMapper feedbackformMapper;
    private final QuestionRowMapper questionMapper;

    private static final Table entityTable = Table.aliased("question", EntityManager.ENTITY_ALIAS);
    private static final Table feedbackFormTable = Table.aliased("feedback_form", "feedbackForm");

    public QuestionRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FeedbackFormRowMapper feedbackformMapper,
        QuestionRowMapper questionMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Question.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.feedbackformMapper = feedbackformMapper;
        this.questionMapper = questionMapper;
    }

    @Override
    public Flux<Question> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Question> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = QuestionSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FeedbackFormSqlHelper.getColumns(feedbackFormTable, "feedbackForm"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(feedbackFormTable)
            .on(Column.create("feedback_form_id", entityTable))
            .equals(Column.create("id", feedbackFormTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Question.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Question> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Question> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Question> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Question> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Question> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Question process(Row row, RowMetadata metadata) {
        Question entity = questionMapper.apply(row, "e");
        entity.setFeedbackForm(feedbackformMapper.apply(row, "feedbackForm"));
        return entity;
    }

    @Override
    public <S extends Question> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
