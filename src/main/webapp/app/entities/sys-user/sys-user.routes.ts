import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SysUserComponent } from './list/sys-user.component';
import { SysUserDetailComponent } from './detail/sys-user-detail.component';
import { SysUserUpdateComponent } from './update/sys-user-update.component';
import SysUserResolve from './route/sys-user-routing-resolve.service';

const sysUserRoute: Routes = [
  {
    path: '',
    component: SysUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SysUserDetailComponent,
    resolve: {
      sysUser: SysUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SysUserUpdateComponent,
    resolve: {
      sysUser: SysUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SysUserUpdateComponent,
    resolve: {
      sysUser: SysUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sysUserRoute;
