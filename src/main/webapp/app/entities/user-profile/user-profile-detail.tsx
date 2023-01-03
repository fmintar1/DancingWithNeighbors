import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="dancingWithNeighborsApp.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="dancingWithNeighborsApp.userProfile.name">Name</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.name}</dd>
          <dt>
            <span id="birthdate">
              <Translate contentKey="dancingWithNeighborsApp.userProfile.birthdate">Birthdate</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.birthdate ? (
              <TextFormat value={userProfileEntity.birthdate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="location">
              <Translate contentKey="dancingWithNeighborsApp.userProfile.location">Location</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.location}</dd>
          <dt>
            <span id="styles">
              <Translate contentKey="dancingWithNeighborsApp.userProfile.styles">Styles</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.styles}</dd>
          <dt>
            <span id="availability">
              <Translate contentKey="dancingWithNeighborsApp.userProfile.availability">Availability</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.availability}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="dancingWithNeighborsApp.userProfile.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.image ? (
              <div>
                {userProfileEntity.imageContentType ? (
                  <a onClick={openFile(userProfileEntity.imageContentType, userProfileEntity.image)}>
                    <img
                      src={`data:${userProfileEntity.imageContentType};base64,${userProfileEntity.image}`}
                      style={{ maxHeight: '30px' }}
                    />
                  </a>
                ) : null}
                <span>
                  {userProfileEntity.imageContentType}, {byteSize(userProfileEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
