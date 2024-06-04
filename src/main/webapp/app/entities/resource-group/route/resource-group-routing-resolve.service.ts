import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResourceGroup } from '../resource-group.model';
import { ResourceGroupService } from '../service/resource-group.service';

const resourceGroupResolve = (route: ActivatedRouteSnapshot): Observable<null | IResourceGroup> => {
  const id = route.params['id'];
  if (id) {
    return inject(ResourceGroupService)
      .find(id)
      .pipe(
        mergeMap((resourceGroup: HttpResponse<IResourceGroup>) => {
          if (resourceGroup.body) {
            return of(resourceGroup.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default resourceGroupResolve;
