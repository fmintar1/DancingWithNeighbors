import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities } from './user-profile.reducer';

export const UserProfile = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const userProfileList = useAppSelector(state => state.userProfile.entities);
  const loading = useAppSelector(state => state.userProfile.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="user-profile-heading" data-cy="UserProfileHeading">
        <Translate contentKey="dancingWithNeighborsApp.userProfile.home.title">User Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dancingWithNeighborsApp.userProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dancingWithNeighborsApp.userProfile.home.createLabel">Create new User Profile</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userProfileList && userProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.userProfile.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.userProfile.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.userProfile.birthdate">Birthdate</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.userProfile.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.userProfile.styles">Styles</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.userProfile.availability">Availability</Translate>
                </th>
                <th>
                  <Translate contentKey="dancingWithNeighborsApp.userProfile.image">Image</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userProfileList.map((userProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="link" size="sm">
                      {userProfile.id}
                    </Button>
                  </td>
                  <td>{userProfile.name}</td>
                  <td>
                    {userProfile.birthdate ? <TextFormat type="date" value={userProfile.birthdate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userProfile.location}</td>
                  <td>
                    <Translate contentKey={`dancingWithNeighborsApp.Styles.${userProfile.styles}`} />
                  </td>
                  <td>{userProfile.availability}</td>
                  <td>
                    {userProfile.image ? (
                      <div>
                        {userProfile.imageContentType ? (
                          <a onClick={openFile(userProfile.imageContentType, userProfile.image)}>
                            <img src={`data:${userProfile.imageContentType};base64,${userProfile.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {userProfile.imageContentType}, {byteSize(userProfile.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-profile/${userProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-profile/${userProfile.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-profile/${userProfile.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="dancingWithNeighborsApp.userProfile.home.notFound">No User Profiles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserProfile;
