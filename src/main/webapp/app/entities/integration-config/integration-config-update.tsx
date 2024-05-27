import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExternalSystem } from 'app/shared/model/external-system.model';
import { getEntities as getExternalSystems } from 'app/entities/external-system/external-system.reducer';
import { IIntegrationConfig } from 'app/shared/model/integration-config.model';
import { getEntity, updateEntity, createEntity, reset } from './integration-config.reducer';

export const IntegrationConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const externalSystems = useAppSelector(state => state.externalSystem.entities);
  const integrationConfigEntity = useAppSelector(state => state.integrationConfig.entity);
  const loading = useAppSelector(state => state.integrationConfig.loading);
  const updating = useAppSelector(state => state.integrationConfig.updating);
  const updateSuccess = useAppSelector(state => state.integrationConfig.updateSuccess);

  const handleClose = () => {
    navigate('/integration-config');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getExternalSystems({}));
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
      ...integrationConfigEntity,
      ...values,
      externalSystem: externalSystems.find(it => it.id.toString() === values.externalSystem?.toString()),
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
          ...integrationConfigEntity,
          createdAt: convertDateTimeFromServer(integrationConfigEntity.createdAt),
          updatedAt: convertDateTimeFromServer(integrationConfigEntity.updatedAt),
          externalSystem: integrationConfigEntity?.externalSystem?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="feedback360App.integrationConfig.home.createOrEditLabel" data-cy="IntegrationConfigCreateUpdateHeading">
            <Translate contentKey="feedback360App.integrationConfig.home.createOrEditLabel">Create or edit a IntegrationConfig</Translate>
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
                  id="integration-config-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('feedback360App.integrationConfig.serviceName')}
                id="integration-config-serviceName"
                name="serviceName"
                data-cy="serviceName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.integrationConfig.configData')}
                id="integration-config-configData"
                name="configData"
                data-cy="configData"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.integrationConfig.createdAt')}
                id="integration-config-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('feedback360App.integrationConfig.updatedAt')}
                id="integration-config-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="integration-config-externalSystem"
                name="externalSystem"
                data-cy="externalSystem"
                label={translate('feedback360App.integrationConfig.externalSystem')}
                type="select"
              >
                <option value="" key="0" />
                {externalSystems
                  ? externalSystems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/integration-config" replace color="info">
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

export default IntegrationConfigUpdate;
