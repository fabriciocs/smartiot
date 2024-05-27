import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DadoSensor from './dado-sensor';
import DadoSensorDetail from './dado-sensor-detail';
import DadoSensorUpdate from './dado-sensor-update';
import DadoSensorDeleteDialog from './dado-sensor-delete-dialog';

const DadoSensorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DadoSensor />} />
    <Route path="new" element={<DadoSensorUpdate />} />
    <Route path=":id">
      <Route index element={<DadoSensorDetail />} />
      <Route path="edit" element={<DadoSensorUpdate />} />
      <Route path="delete" element={<DadoSensorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DadoSensorRoutes;
