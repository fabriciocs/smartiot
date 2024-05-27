import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sys-user.reducer';

export const SysUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sysUserEntity = useAppSelector(state => state.sysUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sysUserDetailsHeading">
          <Translate contentKey="feedback360App.sysUser.detail.title">SysUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sysUserEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="feedback360App.sysUser.name">Name</Translate>
            </span>
          </dt>
          <dd>{sysUserEntity.name}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="feedback360App.sysUser.email">Email</Translate>
            </span>
          </dt>
          <dd>{sysUserEntity.email}</dd>
          <dt>
            <span id="passwordHash">
              <Translate contentKey="feedback360App.sysUser.passwordHash">Password Hash</Translate>
            </span>
          </dt>
          <dd>{sysUserEntity.passwordHash}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="feedback360App.sysUser.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{sysUserEntity.createdAt ? <TextFormat value={sysUserEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="feedback360App.sysUser.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{sysUserEntity.updatedAt ? <TextFormat value={sysUserEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="feedback360App.sysUser.role">Role</Translate>
          </dt>
          <dd>{sysUserEntity.role ? sysUserEntity.role.roleName : ''}</dd>
        </dl>
        <Button tag={Link} to="/sys-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sys-user/${sysUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SysUserDetail;
