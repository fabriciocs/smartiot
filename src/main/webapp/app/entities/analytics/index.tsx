import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Analytics from './analytics';
import AnalyticsDetail from './analytics-detail';
import AnalyticsUpdate from './analytics-update';
import AnalyticsDeleteDialog from './analytics-delete-dialog';

const AnalyticsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Analytics />} />
    <Route path="new" element={<AnalyticsUpdate />} />
    <Route path=":id">
      <Route index element={<AnalyticsDetail />} />
      <Route path="edit" element={<AnalyticsUpdate />} />
      <Route path="delete" element={<AnalyticsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AnalyticsRoutes;
