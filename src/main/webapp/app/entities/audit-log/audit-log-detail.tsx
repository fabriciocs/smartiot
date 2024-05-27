import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './audit-log.reducer';

export const AuditLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const auditLogEntity = useAppSelector(state => state.auditLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="auditLogDetailsHeading">
          <Translate contentKey="feedback360App.auditLog.detail.title">AuditLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.id}</dd>
          <dt>
            <span id="action">
              <Translate contentKey="feedback360App.auditLog.action">Action</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.action}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="feedback360App.auditLog.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.timestamp ? <TextFormat value={auditLogEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="details">
              <Translate contentKey="feedback360App.auditLog.details">Details</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.details}</dd>
          <dt>
            <Translate contentKey="feedback360App.auditLog.user">User</Translate>
          </dt>
          <dd>{auditLogEntity.user ? auditLogEntity.user.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/audit-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/audit-log/${auditLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuditLogDetail;
