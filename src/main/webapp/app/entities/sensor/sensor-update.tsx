import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICliente } from 'app/shared/model/cliente.model';
import { getEntities as getClientes } from 'app/entities/cliente/cliente.reducer';
import { IDadoSensor } from 'app/shared/model/dado-sensor.model';
import { getEntities as getDadoSensors } from 'app/entities/dado-sensor/dado-sensor.reducer';
import { ISensor } from 'app/shared/model/sensor.model';
import { TipoSensor } from 'app/shared/model/enumerations/tipo-sensor.model';
import { getEntity, updateEntity, createEntity, reset } from './sensor.reducer';

export const SensorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const clientes = useAppSelector(state => state.cliente.entities);
  const dadoSensors = useAppSelector(state => state.dadoSensor.entities);
  const sensorEntity = useAppSelector(state => state.sensor.entity);
  const loading = useAppSelector(state => state.sensor.loading);
  const updating = useAppSelector(state => state.sensor.updating);
  const updateSuccess = useAppSelector(state => state.sensor.updateSuccess);
  const tipoSensorValues = Object.keys(TipoSensor);

  const handleClose = () => {
    navigate('/sensor' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getClientes({}));
    dispatch(getDadoSensors({}));
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
      ...sensorEntity,
      ...values,
      cliente: clientes.find(it => it.id.toString() === values.cliente?.toString()),
      dadoSensores: dadoSensors.find(it => it.id.toString() === values.dadoSensores?.toString()),
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
          tipo: 'TEMPERATURE',
          ...sensorEntity,
          cliente: sensorEntity?.cliente?.id,
          dadoSensores: sensorEntity?.dadoSensores?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="feedback360App.sensor.home.createOrEditLabel" data-cy="SensorCreateUpdateHeading">
            <Translate contentKey="feedback360App.sensor.home.createOrEditLabel">Create or edit a Sensor</Translate>
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
                  id="sensor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('feedback360App.sensor.nome')}
                id="sensor-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField label={translate('feedback360App.sensor.tipo')} id="sensor-tipo" name="tipo" data-cy="tipo" type="select">
                {tipoSensorValues.map(tipoSensor => (
                  <option value={tipoSensor} key={tipoSensor}>
                    {translate('feedback360App.TipoSensor.' + tipoSensor)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('feedback360App.sensor.configuracao')}
                id="sensor-configuracao"
                name="configuracao"
                data-cy="configuracao"
                type="text"
              />
              <ValidatedField
                id="sensor-cliente"
                name="cliente"
                data-cy="cliente"
                label={translate('feedback360App.sensor.cliente')}
                type="select"
                required
              >
                <option value="" key="0" />
                {clientes
                  ? clientes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="sensor-dadoSensores"
                name="dadoSensores"
                data-cy="dadoSensores"
                label={translate('feedback360App.sensor.dadoSensores')}
                type="select"
              >
                <option value="" key="0" />
                {dadoSensors
                  ? dadoSensors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.timestamp}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sensor" replace color="info">
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

export default SensorUpdate;
