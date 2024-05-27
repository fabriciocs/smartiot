import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './feedback-form.reducer';

export const FeedbackFormDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const feedbackFormEntity = useAppSelector(state => state.feedbackForm.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="feedbackFormDetailsHeading">
          <Translate contentKey="feedback360App.feedbackForm.detail.title">FeedbackForm</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{feedbackFormEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="feedback360App.feedbackForm.title">Title</Translate>
            </span>
          </dt>
          <dd>{feedbackFormEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="feedback360App.feedbackForm.description">Description</Translate>
            </span>
          </dt>
          <dd>{feedbackFormEntity.description}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="feedback360App.feedbackForm.status">Status</Translate>
            </span>
          </dt>
          <dd>{feedbackFormEntity.status}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="feedback360App.feedbackForm.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {feedbackFormEntity.createdAt ? <TextFormat value={feedbackFormEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="feedback360App.feedbackForm.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {feedbackFormEntity.updatedAt ? <TextFormat value={feedbackFormEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="feedback360App.feedbackForm.creator">Creator</Translate>
          </dt>
          <dd>{feedbackFormEntity.creator ? feedbackFormEntity.creator.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/feedback-form" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/feedback-form/${feedbackFormEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeedbackFormDetail;
