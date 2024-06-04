import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConcentrator, NewConcentrator } from '../concentrator.model';

export type PartialUpdateConcentrator = Partial<IConcentrator> & Pick<IConcentrator, 'id'>;

export type EntityResponseType = HttpResponse<IConcentrator>;
export type EntityArrayResponseType = HttpResponse<IConcentrator[]>;

@Injectable({ providedIn: 'root' })
export class ConcentratorService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/concentrators');

  create(concentrator: NewConcentrator): Observable<EntityResponseType> {
    return this.http.post<IConcentrator>(this.resourceUrl, concentrator, { observe: 'response' });
  }

  update(concentrator: IConcentrator): Observable<EntityResponseType> {
    return this.http.put<IConcentrator>(`${this.resourceUrl}/${this.getConcentratorIdentifier(concentrator)}`, concentrator, {
      observe: 'response',
    });
  }

  partialUpdate(concentrator: PartialUpdateConcentrator): Observable<EntityResponseType> {
    return this.http.patch<IConcentrator>(`${this.resourceUrl}/${this.getConcentratorIdentifier(concentrator)}`, concentrator, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConcentrator>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConcentrator[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConcentratorIdentifier(concentrator: Pick<IConcentrator, 'id'>): number {
    return concentrator.id;
  }

  compareConcentrator(o1: Pick<IConcentrator, 'id'> | null, o2: Pick<IConcentrator, 'id'> | null): boolean {
    return o1 && o2 ? this.getConcentratorIdentifier(o1) === this.getConcentratorIdentifier(o2) : o1 === o2;
  }

  addConcentratorToCollectionIfMissing<Type extends Pick<IConcentrator, 'id'>>(
    concentratorCollection: Type[],
    ...concentratorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const concentrators: Type[] = concentratorsToCheck.filter(isPresent);
    if (concentrators.length > 0) {
      const concentratorCollectionIdentifiers = concentratorCollection.map(concentratorItem =>
        this.getConcentratorIdentifier(concentratorItem),
      );
      const concentratorsToAdd = concentrators.filter(concentratorItem => {
        const concentratorIdentifier = this.getConcentratorIdentifier(concentratorItem);
        if (concentratorCollectionIdentifiers.includes(concentratorIdentifier)) {
          return false;
        }
        concentratorCollectionIdentifiers.push(concentratorIdentifier);
        return true;
      });
      return [...concentratorsToAdd, ...concentratorCollection];
    }
    return concentratorCollection;
  }
}
