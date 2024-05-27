import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISensor } from 'app/shared/model/sensor.model';
import { getEntities as getSensors } from 'app/entities/sensor/sensor.reducer';
import { IConfiguracaoAlerta } from 'app/shared/model/configuracao-alerta.model';
import { getEntity, updateEntity, createEntity, reset } from './configuracao-alerta.reducer';

export const ConfiguracaoAlertaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const sensors = useAppSelector(state => state.sensor.entities);
  const configuracaoAlertaEntity = useAppSelector(state => state.configuracaoAlerta.entity);
  const loading = useAppSelector(state => state.configuracaoAlerta.loading);
  const updating = useAppSelector(state => state.configuracaoAlerta.updating);
  const updateSuccess = useAppSelector(state => state.configuracaoAlerta.updateSuccess);

  const handleClose = () => {
    navigate('/configuracao-alerta' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSensors({}));
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
    if (values.limite !== undefined && typeof values.limite !== 'number') {
      values.limite = Number(values.limite);
    }

    const entity = {
      ...configuracaoAlertaEntity,
      ...values,
      sensor: sensors.find(it => it.id.toString() === values.sensor?.toString()),
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
          ...configuracaoAlertaEntity,
          sensor: configuracaoAlertaEntity?.sensor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="feedback360App.configuracaoAlerta.home.createOrEditLabel" data-cy="ConfiguracaoAlertaCreateUpdateHeading">
            <Translate contentKey="feedback360App.configuracaoAlerta.home.createOrEditLabel">Create or edit a ConfiguracaoAlerta</Translate>
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
                  id="configuracao-alerta-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('feedback360App.configuracaoAlerta.limite')}
                id="configuracao-alerta-limite"
                name="limite"
                data-cy="limite"
                type="text"
              />
              <ValidatedField
                label={translate('feedback360App.configuracaoAlerta.email')}
                id="configuracao-alerta-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: {
                    value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$' }),
                  },
                }}
              />
              <ValidatedField
                id="configuracao-alerta-sensor"
                name="sensor"
                data-cy="sensor"
                label={translate('feedback360App.configuracaoAlerta.sensor')}
                type="select"
                required
              >
                <option value="" key="0" />
                {sensors
                  ? sensors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/configuracao-alerta" replace color="info">
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

export default ConfiguracaoAlertaUpdate;
