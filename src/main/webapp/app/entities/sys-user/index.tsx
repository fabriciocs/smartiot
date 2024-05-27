import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SysUser from './sys-user';
import SysUserDetail from './sys-user-detail';
import SysUserUpdate from './sys-user-update';
import SysUserDeleteDialog from './sys-user-delete-dialog';

const SysUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SysUser />} />
    <Route path="new" element={<SysUserUpdate />} />
    <Route path=":id">
      <Route index element={<SysUserDetail />} />
      <Route path="edit" element={<SysUserUpdate />} />
      <Route path="delete" element={<SysUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SysUserRoutes;
