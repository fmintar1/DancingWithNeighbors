import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Friends from './friends';
import FriendsDetail from './friends-detail';
import FriendsUpdate from './friends-update';
import FriendsDeleteDialog from './friends-delete-dialog';

const FriendsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Friends />} />
    <Route path="new" element={<FriendsUpdate />} />
    <Route path=":id">
      <Route index element={<FriendsDetail />} />
      <Route path="edit" element={<FriendsUpdate />} />
      <Route path="delete" element={<FriendsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FriendsRoutes;
