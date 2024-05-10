import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SensorComponent } from './list/sensor.component';
import { SensorDetailComponent } from './detail/sensor-detail.component';
import { SensorUpdateComponent } from './update/sensor-update.component';
import SensorResolve from './route/sensor-routing-resolve.service';

const sensorRoute: Routes = [
  {
    path: '',
    component: SensorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SensorDetailComponent,
    resolve: {
      sensor: SensorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SensorUpdateComponent,
    resolve: {
      sensor: SensorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SensorUpdateComponent,
    resolve: {
      sensor: SensorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sensorRoute;
