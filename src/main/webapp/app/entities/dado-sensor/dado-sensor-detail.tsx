import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dado-sensor.reducer';

export const DadoSensorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dadoSensorEntity = useAppSelector(state => state.dadoSensor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dadoSensorDetailsHeading">
          <Translate contentKey="feedback360App.dadoSensor.detail.title">DadoSensor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dadoSensorEntity.id}</dd>
          <dt>
            <span id="dados">
              <Translate contentKey="feedback360App.dadoSensor.dados">Dados</Translate>
            </span>
          </dt>
          <dd>{dadoSensorEntity.dados}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="feedback360App.dadoSensor.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {dadoSensorEntity.timestamp ? <TextFormat value={dadoSensorEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/dado-sensor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dado-sensor/${dadoSensorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DadoSensorDetail;
