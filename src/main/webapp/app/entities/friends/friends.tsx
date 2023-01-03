import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFriends } from 'app/shared/model/friends.model';
import { getEntities } from './friends.reducer';

export const Friends = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const friendsList = useAppSelector(state => state.friends.entities);
  const loading = useAppSelector(state => state.friends.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="friends-heading" data-cy="FriendsHeading">
        <Translate contentKey="dancingWithNeighborsApp.friends.home.title">Friends</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dancingWithNeighborsApp.friends.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/friends/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dancingWithNeighborsApp.friends.home.createLabel">Create new Friends</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {friendsList && friendsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.friends.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.friends.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.friends.birthdate">Birthdate</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.friends.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.friends.styles">Styles</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.friends.availability">Availability</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.friends.image">Image</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.friends.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {friendsList.map((friends, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/friends/${friends.id}`} color="link" size="sm">
                      {friends.id}
                    </Button>
                  </td>
                  <td>{friends.name}</td>
                  <td>{friends.birthdate ? <TextFormat type="date" value={friends.birthdate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{friends.location}</td>
                  <td>
                    <Translate contentKey={`dancingWithNeighborsApp.Styles.${friends.styles}`} />
                  </td>
                  <td>{friends.availability}</td>
                  <td>
                    {friends.image ? (
                      <div>
                        {friends.imageContentType ? (
                          <a onClick={openFile(friends.imageContentType, friends.image)}>
                            <img src={`data:${friends.imageContentType};base64,${friends.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {friends.imageContentType}, {byteSize(friends.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{friends.user ? friends.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/friends/${friends.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/friends/${friends.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/friends/${friends.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dancingWithNeighborsApp.friends.home.notFound">No Friends found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Friends;
