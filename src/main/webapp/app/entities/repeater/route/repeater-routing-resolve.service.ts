import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRepeater } from '../repeater.model';
import { RepeaterService } from '../service/repeater.service';

const repeaterResolve = (route: ActivatedRouteSnapshot): Observable<null | IRepeater> => {
  const id = route.params['id'];
  if (id) {
    return inject(RepeaterService)
      .find(id)
      .pipe(
        mergeMap((repeater: HttpResponse<IRepeater>) => {
          if (repeater.body) {
            return of(repeater.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default repeaterResolve;
