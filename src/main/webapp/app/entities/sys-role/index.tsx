import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SysRole from './sys-role';
import SysRoleDetail from './sys-role-detail';
import SysRoleUpdate from './sys-role-update';
import SysRoleDeleteDialog from './sys-role-delete-dialog';

const SysRoleRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SysRole />} />
    <Route path="new" element={<SysRoleUpdate />} />
    <Route path=":id">
      <Route index element={<SysRoleDetail />} />
      <Route path="edit" element={<SysRoleUpdate />} />
      <Route path="delete" element={<SysRoleDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SysRoleRoutes;
