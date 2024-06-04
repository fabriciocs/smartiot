import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAggregatedData } from '../aggregated-data.model';
import { AggregatedDataService } from '../service/aggregated-data.service';

const aggregatedDataResolve = (route: ActivatedRouteSnapshot): Observable<null | IAggregatedData> => {
  const id = route.params['id'];
  if (id) {
    return inject(AggregatedDataService)
      .find(id)
      .pipe(
        mergeMap((aggregatedData: HttpResponse<IAggregatedData>) => {
          if (aggregatedData.body) {
            return of(aggregatedData.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default aggregatedDataResolve;
