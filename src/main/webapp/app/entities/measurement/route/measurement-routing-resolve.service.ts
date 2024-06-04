import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMeasurement } from '../measurement.model';
import { MeasurementService } from '../service/measurement.service';

const measurementResolve = (route: ActivatedRouteSnapshot): Observable<null | IMeasurement> => {
  const id = route.params['id'];
  if (id) {
    return inject(MeasurementService)
      .find(id)
      .pipe(
        mergeMap((measurement: HttpResponse<IMeasurement>) => {
          if (measurement.body) {
            return of(measurement.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default measurementResolve;
