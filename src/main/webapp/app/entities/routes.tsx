import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cliente from './cliente';
import Sensor from './sensor';
import ConfiguracaoAlerta from './configuracao-alerta';
import DadoSensor from './dado-sensor';
import SysUser from './sys-user';
import SysRole from './sys-role';
import Profile from './profile';
import FeedbackForm from './feedback-form';
import Question from './question';
import FeedbackResponse from './feedback-response';
import Notification from './notification';
import NotificationSettings from './notification-settings';
import Report from './report';
import Analytics from './analytics';
import IntegrationConfig from './integration-config';
import ExternalSystem from './external-system';
import AuditLog from './audit-log';
import CompliancePolicy from './compliance-policy';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="cliente/*" element={<Cliente />} />
        <Route path="sensor/*" element={<Sensor />} />
        <Route path="configuracao-alerta/*" element={<ConfiguracaoAlerta />} />
        <Route path="dado-sensor/*" element={<DadoSensor />} />
        <Route path="sys-user/*" element={<SysUser />} />
        <Route path="sys-role/*" element={<SysRole />} />
        <Route path="profile/*" element={<Profile />} />
        <Route path="feedback-form/*" element={<FeedbackForm />} />
        <Route path="question/*" element={<Question />} />
        <Route path="feedback-response/*" element={<FeedbackResponse />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="notification-settings/*" element={<NotificationSettings />} />
        <Route path="report/*" element={<Report />} />
        <Route path="analytics/*" element={<Analytics />} />
        <Route path="integration-config/*" element={<IntegrationConfig />} />
        <Route path="external-system/*" element={<ExternalSystem />} />
        <Route path="audit-log/*" element={<AuditLog />} />
        <Route path="compliance-policy/*" element={<CompliancePolicy />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
