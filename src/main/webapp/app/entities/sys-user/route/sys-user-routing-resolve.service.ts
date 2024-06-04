import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISysUser } from '../sys-user.model';
import { SysUserService } from '../service/sys-user.service';

const sysUserResolve = (route: ActivatedRouteSnapshot): Observable<null | ISysUser> => {
  const id = route.params['id'];
  if (id) {
    return inject(SysUserService)
      .find(id)
      .pipe(
        mergeMap((sysUser: HttpResponse<ISysUser>) => {
          if (sysUser.body) {
            return of(sysUser.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sysUserResolve;
