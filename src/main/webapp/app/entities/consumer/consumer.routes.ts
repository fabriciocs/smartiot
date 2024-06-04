import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ConsumerComponent } from './list/consumer.component';
import { ConsumerDetailComponent } from './detail/consumer-detail.component';
import { ConsumerUpdateComponent } from './update/consumer-update.component';
import ConsumerResolve from './route/consumer-routing-resolve.service';

const consumerRoute: Routes = [
  {
    path: '',
    component: ConsumerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsumerDetailComponent,
    resolve: {
      consumer: ConsumerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsumerUpdateComponent,
    resolve: {
      consumer: ConsumerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsumerUpdateComponent,
    resolve: {
      consumer: ConsumerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default consumerRoute;
