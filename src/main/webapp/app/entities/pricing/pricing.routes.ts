import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PricingComponent } from './list/pricing.component';
import { PricingDetailComponent } from './detail/pricing-detail.component';
import { PricingUpdateComponent } from './update/pricing-update.component';
import PricingResolve from './route/pricing-routing-resolve.service';

const pricingRoute: Routes = [
  {
    path: '',
    component: PricingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PricingDetailComponent,
    resolve: {
      pricing: PricingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PricingUpdateComponent,
    resolve: {
      pricing: PricingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PricingUpdateComponent,
    resolve: {
      pricing: PricingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pricingRoute;
