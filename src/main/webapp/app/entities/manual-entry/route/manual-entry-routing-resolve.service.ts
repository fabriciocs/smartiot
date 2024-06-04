import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManualEntry } from '../manual-entry.model';
import { ManualEntryService } from '../service/manual-entry.service';

const manualEntryResolve = (route: ActivatedRouteSnapshot): Observable<null | IManualEntry> => {
  const id = route.params['id'];
  if (id) {
    return inject(ManualEntryService)
      .find(id)
      .pipe(
        mergeMap((manualEntry: HttpResponse<IManualEntry>) => {
          if (manualEntry.body) {
            return of(manualEntry.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default manualEntryResolve;
