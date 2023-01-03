import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './friends.reducer';

export const FriendsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const friendsEntity = useAppSelector(state => state.friends.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="friendsDetailsHeading">
          <Translate contentKey="dancingWithNeighborsApp.friends.detail.title">Friends</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{friendsEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="dancingWithNeighborsApp.friends.name">Name</Translate>
            </span>
          </dt>
          <dd>{friendsEntity.name}</dd>
          <dt>
            <span id="birthdate">
              <Translate contentKey="dancingWithNeighborsApp.friends.birthdate">Birthdate</Translate>
            </span>
          </dt>
          <dd>
            {friendsEntity.birthdate ? <TextFormat value={friendsEntity.birthdate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="location">
              <Translate contentKey="dancingWithNeighborsApp.friends.location">Location</Translate>
            </span>
          </dt>
          <dd>{friendsEntity.location}</dd>
          <dt>
            <span id="styles">
              <Translate contentKey="dancingWithNeighborsApp.friends.styles">Styles</Translate>
            </span>
          </dt>
          <dd>{friendsEntity.styles}</dd>
          <dt>
            <span id="availability">
              <Translate contentKey="dancingWithNeighborsApp.friends.availability">Availability</Translate>
            </span>
          </dt>
          <dd>{friendsEntity.availability}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="dancingWithNeighborsApp.friends.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {friendsEntity.image ? (
              <div>
                {friendsEntity.imageContentType ? (
                  <a onClick={openFile(friendsEntity.imageContentType, friendsEntity.image)}>
                    <img src={`data:${friendsEntity.imageContentType};base64,${friendsEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {friendsEntity.imageContentType}, {byteSize(friendsEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="dancingWithNeighborsApp.friends.user">User</Translate>
          </dt>
          <dd>{friendsEntity.user ? friendsEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/friends" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/friends/${friendsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FriendsDetail;
