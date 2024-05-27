import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './profile.reducer';

export const ProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const profileEntity = useAppSelector(state => state.profile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="profileDetailsHeading">
          <Translate contentKey="feedback360App.profile.detail.title">Profile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{profileEntity.id}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="feedback360App.profile.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{profileEntity.phoneNumber}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="feedback360App.profile.address">Address</Translate>
            </span>
          </dt>
          <dd>{profileEntity.address}</dd>
          <dt>
            <span id="profilePicture">
              <Translate contentKey="feedback360App.profile.profilePicture">Profile Picture</Translate>
            </span>
          </dt>
          <dd>{profileEntity.profilePicture}</dd>
          <dt>
            <span id="preferences">
              <Translate contentKey="feedback360App.profile.preferences">Preferences</Translate>
            </span>
          </dt>
          <dd>{profileEntity.preferences}</dd>
          <dt>
            <Translate contentKey="feedback360App.profile.user">User</Translate>
          </dt>
          <dd>{profileEntity.user ? profileEntity.user.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/profile/${profileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProfileDetail;
