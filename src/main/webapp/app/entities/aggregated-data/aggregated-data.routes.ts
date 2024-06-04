import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AggregatedDataComponent } from './list/aggregated-data.component';
import { AggregatedDataDetailComponent } from './detail/aggregated-data-detail.component';
import { AggregatedDataUpdateComponent } from './update/aggregated-data-update.component';
import AggregatedDataResolve from './route/aggregated-data-routing-resolve.service';

const aggregatedDataRoute: Routes = [
  {
    path: '',
    component: AggregatedDataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AggregatedDataDetailComponent,
    resolve: {
      aggregatedData: AggregatedDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AggregatedDataUpdateComponent,
    resolve: {
      aggregatedData: AggregatedDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AggregatedDataUpdateComponent,
    resolve: {
      aggregatedData: AggregatedDataResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aggregatedDataRoute;
