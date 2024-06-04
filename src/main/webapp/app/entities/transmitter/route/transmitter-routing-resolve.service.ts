import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransmitter } from '../transmitter.model';
import { TransmitterService } from '../service/transmitter.service';

const transmitterResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransmitter> => {
  const id = route.params['id'];
  if (id) {
    return inject(TransmitterService)
      .find(id)
      .pipe(
        mergeMap((transmitter: HttpResponse<ITransmitter>) => {
          if (transmitter.body) {
            return of(transmitter.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default transmitterResolve;
