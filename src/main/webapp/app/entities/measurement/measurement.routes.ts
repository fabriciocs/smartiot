import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MeasurementComponent } from './list/measurement.component';
import { MeasurementDetailComponent } from './detail/measurement-detail.component';
import { MeasurementUpdateComponent } from './update/measurement-update.component';
import MeasurementResolve from './route/measurement-routing-resolve.service';

const measurementRoute: Routes = [
  {
    path: '',
    component: MeasurementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeasurementDetailComponent,
    resolve: {
      measurement: MeasurementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeasurementUpdateComponent,
    resolve: {
      measurement: MeasurementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeasurementUpdateComponent,
    resolve: {
      measurement: MeasurementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default measurementRoute;
