import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './feedback-response.reducer';

export const FeedbackResponseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const feedbackResponseEntity = useAppSelector(state => state.feedbackResponse.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="feedbackResponseDetailsHeading">
          <Translate contentKey="feedback360App.feedbackResponse.detail.title">FeedbackResponse</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{feedbackResponseEntity.id}</dd>
          <dt>
            <span id="responseData">
              <Translate contentKey="feedback360App.feedbackResponse.responseData">Response Data</Translate>
            </span>
          </dt>
          <dd>{feedbackResponseEntity.responseData}</dd>
          <dt>
            <span id="submittedAt">
              <Translate contentKey="feedback360App.feedbackResponse.submittedAt">Submitted At</Translate>
            </span>
          </dt>
          <dd>
            {feedbackResponseEntity.submittedAt ? (
              <TextFormat value={feedbackResponseEntity.submittedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="feedback360App.feedbackResponse.form">Form</Translate>
          </dt>
          <dd>{feedbackResponseEntity.form ? feedbackResponseEntity.form.title : ''}</dd>
          <dt>
            <Translate contentKey="feedback360App.feedbackResponse.user">User</Translate>
          </dt>
          <dd>{feedbackResponseEntity.user ? feedbackResponseEntity.user.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/feedback-response" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/feedback-response/${feedbackResponseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeedbackResponseDetail;
