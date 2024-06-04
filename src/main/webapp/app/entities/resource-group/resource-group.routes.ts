import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ResourceGroupComponent } from './list/resource-group.component';
import { ResourceGroupDetailComponent } from './detail/resource-group-detail.component';
import { ResourceGroupUpdateComponent } from './update/resource-group-update.component';
import ResourceGroupResolve from './route/resource-group-routing-resolve.service';

const resourceGroupRoute: Routes = [
  {
    path: '',
    component: ResourceGroupComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResourceGroupDetailComponent,
    resolve: {
      resourceGroup: ResourceGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResourceGroupUpdateComponent,
    resolve: {
      resourceGroup: ResourceGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResourceGroupUpdateComponent,
    resolve: {
      resourceGroup: ResourceGroupResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default resourceGroupRoute;
