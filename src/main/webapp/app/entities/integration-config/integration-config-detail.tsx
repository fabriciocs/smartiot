import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './integration-config.reducer';

export const IntegrationConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const integrationConfigEntity = useAppSelector(state => state.integrationConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="integrationConfigDetailsHeading">
          <Translate contentKey="feedback360App.integrationConfig.detail.title">IntegrationConfig</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{integrationConfigEntity.id}</dd>
          <dt>
            <span id="serviceName">
              <Translate contentKey="feedback360App.integrationConfig.serviceName">Service Name</Translate>
            </span>
          </dt>
          <dd>{integrationConfigEntity.serviceName}</dd>
          <dt>
            <span id="configData">
              <Translate contentKey="feedback360App.integrationConfig.configData">Config Data</Translate>
            </span>
          </dt>
          <dd>{integrationConfigEntity.configData}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="feedback360App.integrationConfig.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {integrationConfigEntity.createdAt ? (
              <TextFormat value={integrationConfigEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="feedback360App.integrationConfig.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {integrationConfigEntity.updatedAt ? (
              <TextFormat value={integrationConfigEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="feedback360App.integrationConfig.externalSystem">External System</Translate>
          </dt>
          <dd>{integrationConfigEntity.externalSystem ? integrationConfigEntity.externalSystem.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/integration-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/integration-config/${integrationConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IntegrationConfigDetail;
