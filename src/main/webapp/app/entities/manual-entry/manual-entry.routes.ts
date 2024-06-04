import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ManualEntryComponent } from './list/manual-entry.component';
import { ManualEntryDetailComponent } from './detail/manual-entry-detail.component';
import { ManualEntryUpdateComponent } from './update/manual-entry-update.component';
import ManualEntryResolve from './route/manual-entry-routing-resolve.service';

const manualEntryRoute: Routes = [
  {
    path: '',
    component: ManualEntryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManualEntryDetailComponent,
    resolve: {
      manualEntry: ManualEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManualEntryUpdateComponent,
    resolve: {
      manualEntry: ManualEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManualEntryUpdateComponent,
    resolve: {
      manualEntry: ManualEntryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default manualEntryRoute;
