import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './configuracao-alerta.reducer';

export const ConfiguracaoAlertaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const configuracaoAlertaEntity = useAppSelector(state => state.configuracaoAlerta.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="configuracaoAlertaDetailsHeading">
          <Translate contentKey="feedback360App.configuracaoAlerta.detail.title">ConfiguracaoAlerta</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{configuracaoAlertaEntity.id}</dd>
          <dt>
            <span id="limite">
              <Translate contentKey="feedback360App.configuracaoAlerta.limite">Limite</Translate>
            </span>
          </dt>
          <dd>{configuracaoAlertaEntity.limite}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="feedback360App.configuracaoAlerta.email">Email</Translate>
            </span>
          </dt>
          <dd>{configuracaoAlertaEntity.email}</dd>
          <dt>
            <Translate contentKey="feedback360App.configuracaoAlerta.sensor">Sensor</Translate>
          </dt>
          <dd>{configuracaoAlertaEntity.sensor ? configuracaoAlertaEntity.sensor.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/configuracao-alerta" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/configuracao-alerta/${configuracaoAlertaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ConfiguracaoAlertaDetail;
