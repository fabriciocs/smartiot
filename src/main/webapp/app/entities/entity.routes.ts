import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'smartIoTApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'cliente',
    data: { pageTitle: 'smartIoTApp.cliente.home.title' },
    loadChildren: () => import('./cliente/cliente.routes'),
  },
  {
    path: 'sensor',
    data: { pageTitle: 'smartIoTApp.sensor.home.title' },
    loadChildren: () => import('./sensor/sensor.routes'),
  },
  {
    path: 'configuracao-alerta',
    data: { pageTitle: 'smartIoTApp.configuracaoAlerta.home.title' },
    loadChildren: () => import('./configuracao-alerta/configuracao-alerta.routes'),
  },
  {
    path: 'dado-sensor',
    data: { pageTitle: 'smartIoTApp.dadoSensor.home.title' },
    loadChildren: () => import('./dado-sensor/dado-sensor.routes'),
  },
  {
    path: 'aggregated-data',
    data: { pageTitle: 'smartIoTApp.aggregatedData.home.title' },
    loadChildren: () => import('./aggregated-data/aggregated-data.routes'),
  },
  {
    path: 'alert',
    data: { pageTitle: 'smartIoTApp.alert.home.title' },
    loadChildren: () => import('./alert/alert.routes'),
  },
  {
    path: 'company',
    data: { pageTitle: 'smartIoTApp.company.home.title' },
    loadChildren: () => import('./company/company.routes'),
  },
  {
    path: 'consumer',
    data: { pageTitle: 'smartIoTApp.consumer.home.title' },
    loadChildren: () => import('./consumer/consumer.routes'),
  },
  {
    path: 'cost-center',
    data: { pageTitle: 'smartIoTApp.costCenter.home.title' },
    loadChildren: () => import('./cost-center/cost-center.routes'),
  },
  {
    path: 'enrollment',
    data: { pageTitle: 'smartIoTApp.enrollment.home.title' },
    loadChildren: () => import('./enrollment/enrollment.routes'),
  },
  {
    path: 'manual-entry',
    data: { pageTitle: 'smartIoTApp.manualEntry.home.title' },
    loadChildren: () => import('./manual-entry/manual-entry.routes'),
  },
  {
    path: 'measurement',
    data: { pageTitle: 'smartIoTApp.measurement.home.title' },
    loadChildren: () => import('./measurement/measurement.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'smartIoTApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'payment',
    data: { pageTitle: 'smartIoTApp.payment.home.title' },
    loadChildren: () => import('./payment/payment.routes'),
  },
  {
    path: 'pricing',
    data: { pageTitle: 'smartIoTApp.pricing.home.title' },
    loadChildren: () => import('./pricing/pricing.routes'),
  },
  {
    path: 'report',
    data: { pageTitle: 'smartIoTApp.report.home.title' },
    loadChildren: () => import('./report/report.routes'),
  },
  {
    path: 'resource-group',
    data: { pageTitle: 'smartIoTApp.resourceGroup.home.title' },
    loadChildren: () => import('./resource-group/resource-group.routes'),
  },
  {
    path: 'concentrator',
    data: { pageTitle: 'smartIoTApp.concentrator.home.title' },
    loadChildren: () => import('./concentrator/concentrator.routes'),
  },
  {
    path: 'meter',
    data: { pageTitle: 'smartIoTApp.meter.home.title' },
    loadChildren: () => import('./meter/meter.routes'),
  },
  {
    path: 'repeater',
    data: { pageTitle: 'smartIoTApp.repeater.home.title' },
    loadChildren: () => import('./repeater/repeater.routes'),
  },
  {
    path: 'transmitter',
    data: { pageTitle: 'smartIoTApp.transmitter.home.title' },
    loadChildren: () => import('./transmitter/transmitter.routes'),
  },
  {
    path: 'sys-user',
    data: { pageTitle: 'smartIoTApp.sysUser.home.title' },
    loadChildren: () => import('./sys-user/sys-user.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
