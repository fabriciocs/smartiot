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
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
