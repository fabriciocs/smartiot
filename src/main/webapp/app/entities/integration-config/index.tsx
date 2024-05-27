import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import IntegrationConfig from './integration-config';
import IntegrationConfigDetail from './integration-config-detail';
import IntegrationConfigUpdate from './integration-config-update';
import IntegrationConfigDeleteDialog from './integration-config-delete-dialog';

const IntegrationConfigRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<IntegrationConfig />} />
    <Route path="new" element={<IntegrationConfigUpdate />} />
    <Route path=":id">
      <Route index element={<IntegrationConfigDetail />} />
      <Route path="edit" element={<IntegrationConfigUpdate />} />
      <Route path="delete" element={<IntegrationConfigDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IntegrationConfigRoutes;
