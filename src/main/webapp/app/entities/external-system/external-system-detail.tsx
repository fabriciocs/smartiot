import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './external-system.reducer';

export const ExternalSystemDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const externalSystemEntity = useAppSelector(state => state.externalSystem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="externalSystemDetailsHeading">
          <Translate contentKey="feedback360App.externalSystem.detail.title">ExternalSystem</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{externalSystemEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="feedback360App.externalSystem.name">Name</Translate>
            </span>
          </dt>
          <dd>{externalSystemEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="feedback360App.externalSystem.description">Description</Translate>
            </span>
          </dt>
          <dd>{externalSystemEntity.description}</dd>
          <dt>
            <span id="apiEndpoint">
              <Translate contentKey="feedback360App.externalSystem.apiEndpoint">Api Endpoint</Translate>
            </span>
          </dt>
          <dd>{externalSystemEntity.apiEndpoint}</dd>
          <dt>
            <span id="authDetails">
              <Translate contentKey="feedback360App.externalSystem.authDetails">Auth Details</Translate>
            </span>
          </dt>
          <dd>{externalSystemEntity.authDetails}</dd>
        </dl>
        <Button tag={Link} to="/external-system" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/external-system/${externalSystemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExternalSystemDetail;
