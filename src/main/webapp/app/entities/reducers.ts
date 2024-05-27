import cliente from 'app/entities/cliente/cliente.reducer';
import sensor from 'app/entities/sensor/sensor.reducer';
import configuracaoAlerta from 'app/entities/configuracao-alerta/configuracao-alerta.reducer';
import dadoSensor from 'app/entities/dado-sensor/dado-sensor.reducer';
import sysUser from 'app/entities/sys-user/sys-user.reducer';
import sysRole from 'app/entities/sys-role/sys-role.reducer';
import profile from 'app/entities/profile/profile.reducer';
import feedbackForm from 'app/entities/feedback-form/feedback-form.reducer';
import question from 'app/entities/question/question.reducer';
import feedbackResponse from 'app/entities/feedback-response/feedback-response.reducer';
import notification from 'app/entities/notification/notification.reducer';
import notificationSettings from 'app/entities/notification-settings/notification-settings.reducer';
import report from 'app/entities/report/report.reducer';
import analytics from 'app/entities/analytics/analytics.reducer';
import integrationConfig from 'app/entities/integration-config/integration-config.reducer';
import externalSystem from 'app/entities/external-system/external-system.reducer';
import auditLog from 'app/entities/audit-log/audit-log.reducer';
import compliancePolicy from 'app/entities/compliance-policy/compliance-policy.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  cliente,
  sensor,
  configuracaoAlerta,
  dadoSensor,
  sysUser,
  sysRole,
  profile,
  feedbackForm,
  question,
  feedbackResponse,
  notification,
  notificationSettings,
  report,
  analytics,
  integrationConfig,
  externalSystem,
  auditLog,
  compliancePolicy,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
