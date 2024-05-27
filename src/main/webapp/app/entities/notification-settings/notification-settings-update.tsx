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
import { INotificationSettings } from 'app/shared/model/notification-settings.model';
import { getEntity, updateEntity, createEntity, reset } from './notification-settings.reducer';

export const NotificationSettingsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const sysUsers = useAppSelector(state => state.sysUser.entities);
  const notificationSettingsEntity = useAppSelector(state => state.notificationSettings.entity);
  const loading = useAppSelector(state => state.notificationSettings.loading);
  const updating = useAppSelector(state => state.notificationSettings.updating);
  const updateSuccess = useAppSelector(state => state.notificationSettings.updateSuccess);

  const handleClose = () => {
    navigate('/notification-settings');
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

    const entity = {
      ...notificationSettingsEntity,
      ...values,
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
      ? {}
      : {
          ...notificationSettingsEntity,
          user: notificationSettingsEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="feedback360App.notificationSettings.home.createOrEditLabel" data-cy="NotificationSettingsCreateUpdateHeading">
            <Translate contentKey="feedback360App.notificationSettings.home.createOrEditLabel">
              Create or edit a NotificationSettings
            </Translate>
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
                  id="notification-settings-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('feedback360App.notificationSettings.preferences')}
                id="notification-settings-preferences"
                name="preferences"
                data-cy="preferences"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="notification-settings-user"
                name="user"
                data-cy="user"
                label={translate('feedback360App.notificationSettings.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/notification-settings" replace color="info">
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

export default NotificationSettingsUpdate;
