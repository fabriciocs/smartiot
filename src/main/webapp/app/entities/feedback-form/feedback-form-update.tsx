import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISysUser } from 'app/shared/model/sys-user.model';
import { getEntities as getSysUsers } from 'app/entities/sys-user/sys-user.reducer';
import { IFeedbackForm } from 'app/shared/model/feedback-form.model';
import { getEntity, updateEntity, createEntity, reset } from './feedback-form.reducer';

export const FeedbackFormUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const sysUsers = useAppSelector(state => state.sysUser.entities);
  const feedbackFormEntity = useAppSelector(state => state.feedbackForm.entity);
  const loading = useAppSelector(state => state.feedbackForm.loading);
  const updating = useAppSelector(state => state.feedbackForm.updating);
  const updateSuccess = useAppSelector(state => state.feedbackForm.updateSuccess);

  const handleClose = () => {
    navigate('/feedback-form' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...feedbackFormEntity,
      ...values,
      creator: sysUsers.find(it => it.id.toString() === values.creator?.toString()),
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
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...feedbackFormEntity,
          createdAt: convertDateTimeFromServer(feedbackFormEntity.createdAt),
          updatedAt: convertDateTimeFromServer(feedbackFormEntity.updatedAt),
          creator: feedbackFormEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="feedback360App.feedbackForm.home.createOrEditLabel" data-cy="FeedbackFormCreateUpdateHeading">
            <Translate contentKey="feedback360App.feedbackForm.home.createOrEditLabel">Create or edit a FeedbackForm</Translate>
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
                  id="feedback-form-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('feedback360App.feedbackForm.title')}
                id="feedback-form-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.feedbackForm.description')}
                id="feedback-form-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('feedback360App.feedbackForm.status')}
                id="feedback-form-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.feedbackForm.createdAt')}
                id="feedback-form-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.feedbackForm.updatedAt')}
                id="feedback-form-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="feedback-form-creator"
                name="creator"
                data-cy="creator"
                label={translate('feedback360App.feedbackForm.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/feedback-form" replace color="info">
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

export default FeedbackFormUpdate;
