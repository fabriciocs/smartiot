import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFeedbackForm } from 'app/shared/model/feedback-form.model';
import { getEntities as getFeedbackForms } from 'app/entities/feedback-form/feedback-form.reducer';
import { ISysUser } from 'app/shared/model/sys-user.model';
import { getEntities as getSysUsers } from 'app/entities/sys-user/sys-user.reducer';
import { IFeedbackResponse } from 'app/shared/model/feedback-response.model';
import { getEntity, updateEntity, createEntity, reset } from './feedback-response.reducer';

export const FeedbackResponseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const feedbackForms = useAppSelector(state => state.feedbackForm.entities);
  const sysUsers = useAppSelector(state => state.sysUser.entities);
  const feedbackResponseEntity = useAppSelector(state => state.feedbackResponse.entity);
  const loading = useAppSelector(state => state.feedbackResponse.loading);
  const updating = useAppSelector(state => state.feedbackResponse.updating);
  const updateSuccess = useAppSelector(state => state.feedbackResponse.updateSuccess);

  const handleClose = () => {
    navigate('/feedback-response' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFeedbackForms({}));
    dispatch(getSysUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.submittedAt = convertDateTimeToServer(values.submittedAt);

    const entity = {
      ...feedbackResponseEntity,
      ...values,
      form: feedbackForms.find(it => it.id.toString() === values.form?.toString()),
      user: sysUsers.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          submittedAt: displayDefaultDateTime(),
        }
      : {
          ...feedbackResponseEntity,
          submittedAt: convertDateTimeFromServer(feedbackResponseEntity.submittedAt),
          form: feedbackResponseEntity?.form?.id,
          user: feedbackResponseEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="feedback360App.feedbackResponse.home.createOrEditLabel" data-cy="FeedbackResponseCreateUpdateHeading">
            <Translate contentKey="feedback360App.feedbackResponse.home.createOrEditLabel">Create or edit a FeedbackResponse</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="feedback-response-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('feedback360App.feedbackResponse.responseData')}
                id="feedback-response-responseData"
                name="responseData"
                data-cy="responseData"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.feedbackResponse.submittedAt')}
                id="feedback-response-submittedAt"
                name="submittedAt"
                data-cy="submittedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="feedback-response-form"
                name="form"
                data-cy="form"
                label={translate('feedback360App.feedbackResponse.form')}
                type="select"
              >
                <option value="" key="0" />
                {feedbackForms
                  ? feedbackForms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="feedback-response-user"
                name="user"
                data-cy="user"
                label={translate('feedback360App.feedbackResponse.user')}
                type="select"
              >
                <option value="" key="0" />
                {sysUsers
                  ? sysUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/feedback-response" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FeedbackResponseUpdate;
