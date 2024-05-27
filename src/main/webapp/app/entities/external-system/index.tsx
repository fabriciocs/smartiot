import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ExternalSystem from './external-system';
import ExternalSystemDetail from './external-system-detail';
import ExternalSystemUpdate from './external-system-update';
import ExternalSystemDeleteDialog from './external-system-delete-dialog';

const ExternalSystemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExternalSystem />} />
    <Route path="new" element={<ExternalSystemUpdate />} />
    <Route path=":id">
      <Route index element={<ExternalSystemDetail />} />
      <Route path="edit" element={<ExternalSystemUpdate />} />
      <Route path="delete" element={<ExternalSystemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExternalSystemRoutes;
