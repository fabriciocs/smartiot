import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDadoSensor } from '../dado-sensor.model';
import { DadoSensorService } from '../service/dado-sensor.service';

const dadoSensorResolve = (route: ActivatedRouteSnapshot): Observable<null | IDadoSensor> => {
  const id = route.params['id'];
  if (id) {
    return inject(DadoSensorService)
      .find(id)
      .pipe(
        mergeMap((dadoSensor: HttpResponse<IDadoSensor>) => {
          if (dadoSensor.body) {
            return of(dadoSensor.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default dadoSensorResolve;
