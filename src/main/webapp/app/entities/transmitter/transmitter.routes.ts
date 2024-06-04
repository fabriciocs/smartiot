import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TransmitterComponent } from './list/transmitter.component';
import { TransmitterDetailComponent } from './detail/transmitter-detail.component';
import { TransmitterUpdateComponent } from './update/transmitter-update.component';
import TransmitterResolve from './route/transmitter-routing-resolve.service';

const transmitterRoute: Routes = [
  {
    path: '',
    component: TransmitterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransmitterDetailComponent,
    resolve: {
      transmitter: TransmitterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransmitterUpdateComponent,
    resolve: {
      transmitter: TransmitterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransmitterUpdateComponent,
    resolve: {
      transmitter: TransmitterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transmitterRoute;
