import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConfiguracaoAlerta } from '../configuracao-alerta.model';
import { ConfiguracaoAlertaService } from '../service/configuracao-alerta.service';

const configuracaoAlertaResolve = (route: ActivatedRouteSnapshot): Observable<null | IConfiguracaoAlerta> => {
  const id = route.params['id'];
  if (id) {
    return inject(ConfiguracaoAlertaService)
      .find(id)
      .pipe(
        mergeMap((configuracaoAlerta: HttpResponse<IConfiguracaoAlerta>) => {
          if (configuracaoAlerta.body) {
            return of(configuracaoAlerta.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default configuracaoAlertaResolve;
