package br.com.supera.feedback360.repository;

import br.com.supera.feedback360.domain.SysUser;
import br.com.supera.feedback360.repository.rowmapper.SysRoleRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the SysUser entity.
 */
@SuppressWarnings("unused")
class SysUserRepositoryInternalImpl extends SimpleR2dbcRepository<SysUser, Long> implements SysUserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SysRoleRowMapper sysroleMapper;
    private final SysUserRowMapper sysuserMapper;

    private static final Table entityTable = Table.aliased("sys_user", EntityManager.ENTITY_ALIAS);
    private static final Table roleTable = Table.aliased("sys_role", "e_role");

    public SysUserRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SysRoleRowMapper sysroleMapper,
        SysUserRowMapper sysuserMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(SysUser.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.sysroleMapper = sysroleMapper;
        this.sysuserMapper = sysuserMapper;
    }

    @Override
    public Flux<SysUser> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<SysUser> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SysUserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SysRoleSqlHelper.getColumns(roleTable, "role"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(roleTable)
            .on(Column.create("role_id", entityTable))
            .equals(Column.create("id", roleTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, SysUser.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<SysUser> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<SysUser> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<SysUser> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<SysUser> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<SysUser> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private SysUser process(Row row, RowMetadata metadata) {
        SysUser entity = sysuserMapper.apply(row, "e");
        entity.setRole(sysroleMapper.apply(row, "role"));
        return entity;
    }

    @Override
    public <S extends SysUser> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
