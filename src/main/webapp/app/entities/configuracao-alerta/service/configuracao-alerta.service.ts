import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConfiguracaoAlerta, NewConfiguracaoAlerta } from '../configuracao-alerta.model';

export type PartialUpdateConfiguracaoAlerta = Partial<IConfiguracaoAlerta> & Pick<IConfiguracaoAlerta, 'id'>;

export type EntityResponseType = HttpResponse<IConfiguracaoAlerta>;
export type EntityArrayResponseType = HttpResponse<IConfiguracaoAlerta[]>;

@Injectable({ providedIn: 'root' })
export class ConfiguracaoAlertaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/configuracao-alertas');

  create(configuracaoAlerta: NewConfiguracaoAlerta): Observable<EntityResponseType> {
    return this.http.post<IConfiguracaoAlerta>(this.resourceUrl, configuracaoAlerta, { observe: 'response' });
  }

  update(configuracaoAlerta: IConfiguracaoAlerta): Observable<EntityResponseType> {
    return this.http.put<IConfiguracaoAlerta>(
      `${this.resourceUrl}/${this.getConfiguracaoAlertaIdentifier(configuracaoAlerta)}`,
      configuracaoAlerta,
      { observe: 'response' },
    );
  }

  partialUpdate(configuracaoAlerta: PartialUpdateConfiguracaoAlerta): Observable<EntityResponseType> {
    return this.http.patch<IConfiguracaoAlerta>(
      `${this.resourceUrl}/${this.getConfiguracaoAlertaIdentifier(configuracaoAlerta)}`,
      configuracaoAlerta,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfiguracaoAlerta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfiguracaoAlerta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConfiguracaoAlertaIdentifier(configuracaoAlerta: Pick<IConfiguracaoAlerta, 'id'>): number {
    return configuracaoAlerta.id;
  }

  compareConfiguracaoAlerta(o1: Pick<IConfiguracaoAlerta, 'id'> | null, o2: Pick<IConfiguracaoAlerta, 'id'> | null): boolean {
    return o1 && o2 ? this.getConfiguracaoAlertaIdentifier(o1) === this.getConfiguracaoAlertaIdentifier(o2) : o1 === o2;
  }

  addConfiguracaoAlertaToCollectionIfMissing<Type extends Pick<IConfiguracaoAlerta, 'id'>>(
    configuracaoAlertaCollection: Type[],
    ...configuracaoAlertasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const configuracaoAlertas: Type[] = configuracaoAlertasToCheck.filter(isPresent);
    if (configuracaoAlertas.length > 0) {
      const configuracaoAlertaCollectionIdentifiers = configuracaoAlertaCollection.map(configuracaoAlertaItem =>
        this.getConfiguracaoAlertaIdentifier(configuracaoAlertaItem),
      );
      const configuracaoAlertasToAdd = configuracaoAlertas.filter(configuracaoAlertaItem => {
        const configuracaoAlertaIdentifier = this.getConfiguracaoAlertaIdentifier(configuracaoAlertaItem);
        if (configuracaoAlertaCollectionIdentifiers.includes(configuracaoAlertaIdentifier)) {
          return false;
        }
        configuracaoAlertaCollectionIdentifiers.push(configuracaoAlertaIdentifier);
        return true;
      });
      return [...configuracaoAlertasToAdd, ...configuracaoAlertaCollection];
    }
    return configuracaoAlertaCollection;
  }
}
