import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MeterComponent } from './list/meter.component';
import { MeterDetailComponent } from './detail/meter-detail.component';
import { MeterUpdateComponent } from './update/meter-update.component';
import MeterResolve from './route/meter-routing-resolve.service';

const meterRoute: Routes = [
  {
    path: '',
    component: MeterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeterDetailComponent,
    resolve: {
      meter: MeterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeterUpdateComponent,
    resolve: {
      meter: MeterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeterUpdateComponent,
    resolve: {
      meter: MeterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default meterRoute;
