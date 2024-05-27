package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.NotificationSettings;
import br.com.supera.feedback360.repository.rowmapper.NotificationSettingsRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the NotificationSettings entity.
 */
@SuppressWarnings("unused")
class NotificationSettingsRepositoryInternalImpl
    extends SimpleR2dbcRepository<NotificationSettings, Long>
    implements NotificationSettingsRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SysUserRowMapper sysuserMapper;
    private final NotificationSettingsRowMapper notificationsettingsMapper;

    private static final Table entityTable = Table.aliased("notification_settings", EntityManager.ENTITY_ALIAS);
    private static final Table userTable = Table.aliased("sys_user", "e_user");

    public NotificationSettingsRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SysUserRowMapper sysuserMapper,
        NotificationSettingsRowMapper notificationsettingsMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(NotificationSettings.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sysuserMapper = sysuserMapper;
        this.notificationsettingsMapper = notificationsettingsMapper;
    }

    @Override
    public Flux<NotificationSettings> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<NotificationSettings> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = NotificationSettingsSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SysUserSqlHelper.getColumns(userTable, "user"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(userTable)
            .on(Column.create("user_id", entityTable))
            .equals(Column.create("id", userTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, NotificationSettings.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<NotificationSettings> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<NotificationSettings> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<NotificationSettings> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<NotificationSettings> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<NotificationSettings> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private NotificationSettings process(Row row, RowMetadata metadata) {
        NotificationSettings entity = notificationsettingsMapper.apply(row, "e");
        entity.setUser(sysuserMapper.apply(row, "user"));
        return entity;
    }

    @Override
    public <S extends NotificationSettings> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
