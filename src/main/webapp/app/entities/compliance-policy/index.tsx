import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompliancePolicy from './compliance-policy';
import CompliancePolicyDetail from './compliance-policy-detail';
import CompliancePolicyUpdate from './compliance-policy-update';
import CompliancePolicyDeleteDialog from './compliance-policy-delete-dialog';

const CompliancePolicyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompliancePolicy />} />
    <Route path="new" element={<CompliancePolicyUpdate />} />
    <Route path=":id">
      <Route index element={<CompliancePolicyDetail />} />
      <Route path="edit" element={<CompliancePolicyUpdate />} />
      <Route path="delete" element={<CompliancePolicyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompliancePolicyRoutes;
