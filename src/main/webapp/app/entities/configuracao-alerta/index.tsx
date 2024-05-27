import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ConfiguracaoAlerta from './configuracao-alerta';
import ConfiguracaoAlertaDetail from './configuracao-alerta-detail';
import ConfiguracaoAlertaUpdate from './configuracao-alerta-update';
import ConfiguracaoAlertaDeleteDialog from './configuracao-alerta-delete-dialog';

const ConfiguracaoAlertaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ConfiguracaoAlerta />} />
    <Route path="new" element={<ConfiguracaoAlertaUpdate />} />
    <Route path=":id">
      <Route index element={<ConfiguracaoAlertaDetail />} />
      <Route path="edit" element={<ConfiguracaoAlertaUpdate />} />
      <Route path="delete" element={<ConfiguracaoAlertaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ConfiguracaoAlertaRoutes;
