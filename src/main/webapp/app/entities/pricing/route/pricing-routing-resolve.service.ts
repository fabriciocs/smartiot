import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPricing } from '../pricing.model';
import { PricingService } from '../service/pricing.service';

const pricingResolve = (route: ActivatedRouteSnapshot): Observable<null | IPricing> => {
  const id = route.params['id'];
  if (id) {
    return inject(PricingService)
      .find(id)
      .pipe(
        mergeMap((pricing: HttpResponse<IPricing>) => {
          if (pricing.body) {
            return of(pricing.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default pricingResolve;
