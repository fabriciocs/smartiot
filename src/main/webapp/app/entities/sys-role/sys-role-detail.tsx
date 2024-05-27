import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sys-role.reducer';

export const SysRoleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sysRoleEntity = useAppSelector(state => state.sysRole.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sysRoleDetailsHeading">
          <Translate contentKey="feedback360App.sysRole.detail.title">SysRole</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sysRoleEntity.id}</dd>
          <dt>
            <span id="roleName">
              <Translate contentKey="feedback360App.sysRole.roleName">Role Name</Translate>
            </span>
          </dt>
          <dd>{sysRoleEntity.roleName}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="feedback360App.sysRole.description">Description</Translate>
            </span>
          </dt>
          <dd>{sysRoleEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/sys-role" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sys-role/${sysRoleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SysRoleDetail;
