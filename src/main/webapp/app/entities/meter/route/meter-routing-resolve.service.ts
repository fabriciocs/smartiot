import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeter } from '../meter.model';
import { MeterService } from '../service/meter.service';

const meterResolve = (route: ActivatedRouteSnapshot): Observable<null | IMeter> => {
  const id = route.params['id'];
  if (id) {
    return inject(MeterService)
      .find(id)
      .pipe(
        mergeMap((meter: HttpResponse<IMeter>) => {
          if (meter.body) {
            return of(meter.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default meterResolve;
