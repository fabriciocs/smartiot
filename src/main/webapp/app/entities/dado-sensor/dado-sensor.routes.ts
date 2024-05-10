import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DadoSensorComponent } from './list/dado-sensor.component';
import { DadoSensorDetailComponent } from './detail/dado-sensor-detail.component';
import { DadoSensorUpdateComponent } from './update/dado-sensor-update.component';
import DadoSensorResolve from './route/dado-sensor-routing-resolve.service';

const dadoSensorRoute: Routes = [
  {
    path: '',
    component: DadoSensorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DadoSensorDetailComponent,
    resolve: {
      dadoSensor: DadoSensorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DadoSensorUpdateComponent,
    resolve: {
      dadoSensor: DadoSensorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DadoSensorUpdateComponent,
    resolve: {
      dadoSensor: DadoSensorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dadoSensorRoute;
