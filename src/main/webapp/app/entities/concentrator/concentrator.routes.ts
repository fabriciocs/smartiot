import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConcentratorComponent } from './list/concentrator.component';
import { ConcentratorDetailComponent } from './detail/concentrator-detail.component';
import { ConcentratorUpdateComponent } from './update/concentrator-update.component';
import ConcentratorResolve from './route/concentrator-routing-resolve.service';

const concentratorRoute: Routes = [
  {
    path: '',
    component: ConcentratorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConcentratorDetailComponent,
    resolve: {
      concentrator: ConcentratorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConcentratorUpdateComponent,
    resolve: {
      concentrator: ConcentratorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConcentratorUpdateComponent,
    resolve: {
      concentrator: ConcentratorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default concentratorRoute;
