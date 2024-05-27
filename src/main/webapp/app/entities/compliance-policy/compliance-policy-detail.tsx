import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './compliance-policy.reducer';

export const CompliancePolicyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const compliancePolicyEntity = useAppSelector(state => state.compliancePolicy.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="compliancePolicyDetailsHeading">
          <Translate contentKey="feedback360App.compliancePolicy.detail.title">CompliancePolicy</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{compliancePolicyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="feedback360App.compliancePolicy.name">Name</Translate>
            </span>
          </dt>
          <dd>{compliancePolicyEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="feedback360App.compliancePolicy.description">Description</Translate>
            </span>
          </dt>
          <dd>{compliancePolicyEntity.description}</dd>
          <dt>
            <span id="rules">
              <Translate contentKey="feedback360App.compliancePolicy.rules">Rules</Translate>
            </span>
          </dt>
          <dd>{compliancePolicyEntity.rules}</dd>
        </dl>
        <Button tag={Link} to="/compliance-policy" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/compliance-policy/${compliancePolicyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompliancePolicyDetail;
