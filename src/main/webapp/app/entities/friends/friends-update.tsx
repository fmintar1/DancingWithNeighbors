import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IFriends } from 'app/shared/model/friends.model';
import { Styles } from 'app/shared/model/enumerations/styles.model';
import { getEntity, updateEntity, createEntity, reset } from './friends.reducer';

export const FriendsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const friendsEntity = useAppSelector(state => state.friends.entity);
  const loading = useAppSelector(state => state.friends.loading);
  const updating = useAppSelector(state => state.friends.updating);
  const updateSuccess = useAppSelector(state => state.friends.updateSuccess);
  const stylesValues = Object.keys(Styles);

  const handleClose = () => {
    navigate('/friends');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...friendsEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          styles: 'BACHATA',
          ...friendsEntity,
          user: friendsEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dancingWithNeighborsApp.friends.home.createOrEditLabel" data-cy="FriendsCreateUpdateHeading">
            <Translate contentKey="dancingWithNeighborsApp.friends.home.createOrEditLabel">Create or edit a Friends</Translate>
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
                  id="friends-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dancingWithNeighborsApp.friends.name')}
                id="friends-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('dancingWithNeighborsApp.friends.birthdate')}
                id="friends-birthdate"
                name="birthdate"
                data-cy="birthdate"
                type="date"
              />
              <ValidatedField
                label={translate('dancingWithNeighborsApp.friends.location')}
                id="friends-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('dancingWithNeighborsApp.friends.styles')}
                id="friends-styles"
                name="styles"
                data-cy="styles"
                type="select"
              >
                {stylesValues.map(styles => (
                  <option value={styles} key={styles}>
                    {translate('dancingWithNeighborsApp.Styles.' + styles)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('dancingWithNeighborsApp.friends.availability')}
                id="friends-availability"
                name="availability"
                data-cy="availability"
                type="text"
              />
              <ValidatedBlobField
                label={translate('dancingWithNeighborsApp.friends.image')}
                id="friends-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField
                id="friends-user"
                name="user"
                data-cy="user"
                label={translate('dancingWithNeighborsApp.friends.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/friends" replace color="info">
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

export default FriendsUpdate;
