import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './analytics.reducer';

export const AnalyticsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const analyticsEntity = useAppSelector(state => state.analytics.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="analyticsDetailsHeading">
          <Translate contentKey="feedback360App.analytics.detail.title">Analytics</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{analyticsEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="feedback360App.analytics.type">Type</Translate>
            </span>
          </dt>
          <dd>{analyticsEntity.type}</dd>
          <dt>
            <span id="data">
              <Translate contentKey="feedback360App.analytics.data">Data</Translate>
            </span>
          </dt>
          <dd>{analyticsEntity.data}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="feedback360App.analytics.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {analyticsEntity.createdAt ? <TextFormat value={analyticsEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/analytics" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/analytics/${analyticsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AnalyticsDetail;
