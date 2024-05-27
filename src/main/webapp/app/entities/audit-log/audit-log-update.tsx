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
import { IAuditLog } from 'app/shared/model/audit-log.model';
import { getEntity, updateEntity, createEntity, reset } from './audit-log.reducer';

export const AuditLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const sysUsers = useAppSelector(state => state.sysUser.entities);
  const auditLogEntity = useAppSelector(state => state.auditLog.entity);
  const loading = useAppSelector(state => state.auditLog.loading);
  const updating = useAppSelector(state => state.auditLog.updating);
  const updateSuccess = useAppSelector(state => state.auditLog.updateSuccess);

  const handleClose = () => {
    navigate('/audit-log' + location.search);
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
    values.timestamp = convertDateTimeToServer(values.timestamp);

    const entity = {
      ...auditLogEntity,
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
      ? {
          timestamp: displayDefaultDateTime(),
        }
      : {
          ...auditLogEntity,
          timestamp: convertDateTimeFromServer(auditLogEntity.timestamp),
          user: auditLogEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="feedback360App.auditLog.home.createOrEditLabel" data-cy="AuditLogCreateUpdateHeading">
            <Translate contentKey="feedback360App.auditLog.home.createOrEditLabel">Create or edit a AuditLog</Translate>
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
                  id="audit-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('feedback360App.auditLog.action')}
                id="audit-log-action"
                name="action"
                data-cy="action"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.auditLog.timestamp')}
                id="audit-log-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.auditLog.details')}
                id="audit-log-details"
                name="details"
                data-cy="details"
                type="text"
              />
              <ValidatedField
                id="audit-log-user"
                name="user"
                data-cy="user"
                label={translate('feedback360App.auditLog.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/audit-log" replace color="info">
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

export default AuditLogUpdate;
