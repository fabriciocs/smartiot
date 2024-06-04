import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RepeaterComponent } from './list/repeater.component';
import { RepeaterDetailComponent } from './detail/repeater-detail.component';
import { RepeaterUpdateComponent } from './update/repeater-update.component';
import RepeaterResolve from './route/repeater-routing-resolve.service';

const repeaterRoute: Routes = [
  {
    path: '',
    component: RepeaterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RepeaterDetailComponent,
    resolve: {
      repeater: RepeaterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RepeaterUpdateComponent,
    resolve: {
      repeater: RepeaterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RepeaterUpdateComponent,
    resolve: {
      repeater: RepeaterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default repeaterRoute;
