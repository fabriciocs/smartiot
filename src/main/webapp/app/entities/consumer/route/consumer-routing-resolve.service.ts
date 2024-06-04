import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsumer } from '../consumer.model';
import { ConsumerService } from '../service/consumer.service';

const consumerResolve = (route: ActivatedRouteSnapshot): Observable<null | IConsumer> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConsumerService)
      .find(id)
      .pipe(
        mergeMap((consumer: HttpResponse<IConsumer>) => {
          if (consumer.body) {
            return of(consumer.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default consumerResolve;
