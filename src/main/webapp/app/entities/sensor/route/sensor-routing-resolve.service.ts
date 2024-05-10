import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISensor } from '../sensor.model';
import { SensorService } from '../service/sensor.service';

const sensorResolve = (route: ActivatedRouteSnapshot): Observable<null | ISensor> => {
  const id = route.params['id'];
  if (id) {
    return inject(SensorService)
      .find(id)
      .pipe(
        mergeMap((sensor: HttpResponse<ISensor>) => {
          if (sensor.body) {
            return of(sensor.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default sensorResolve;
