import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { Styles } from 'app/shared/model/enumerations/styles.model';
import { getEntity, updateEntity, createEntity, reset } from './user-profile.reducer';

export const UserProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  const loading = useAppSelector(state => state.userProfile.loading);
  const updating = useAppSelector(state => state.userProfile.updating);
  const updateSuccess = useAppSelector(state => state.userProfile.updateSuccess);
  const stylesValues = Object.keys(Styles);

  const handleClose = () => {
    navigate('/user-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...userProfileEntity,
      ...values,
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
          ...userProfileEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dancingWithNeighborsApp.userProfile.home.createOrEditLabel" data-cy="UserProfileCreateUpdateHeading">
            <Translate contentKey="dancingWithNeighborsApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
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
                  id="user-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dancingWithNeighborsApp.userProfile.name')}
                id="user-profile-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('dancingWithNeighborsApp.userProfile.birthdate')}
                id="user-profile-birthdate"
                name="birthdate"
                data-cy="birthdate"
                type="date"
              />
              <ValidatedField
                label={translate('dancingWithNeighborsApp.userProfile.location')}
                id="user-profile-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('dancingWithNeighborsApp.userProfile.styles')}
                id="user-profile-styles"
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
                label={translate('dancingWithNeighborsApp.userProfile.availability')}
                id="user-profile-availability"
                name="availability"
                data-cy="availability"
                type="text"
              />
              <ValidatedBlobField
                label={translate('dancingWithNeighborsApp.userProfile.image')}
                id="user-profile-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-profile" replace color="info">
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

export default UserProfileUpdate;
