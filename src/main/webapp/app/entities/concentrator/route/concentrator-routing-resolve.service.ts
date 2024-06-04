import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConcentrator } from '../concentrator.model';
import { ConcentratorService } from '../service/concentrator.service';

const concentratorResolve = (route: ActivatedRouteSnapshot): Observable<null | IConcentrator> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConcentratorService)
      .find(id)
      .pipe(
        mergeMap((concentrator: HttpResponse<IConcentrator>) => {
          if (concentrator.body) {
            return of(concentrator.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default concentratorResolve;
