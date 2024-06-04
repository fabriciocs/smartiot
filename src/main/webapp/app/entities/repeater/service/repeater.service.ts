import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRepeater, NewRepeater } from '../repeater.model';

export type PartialUpdateRepeater = Partial<IRepeater> & Pick<IRepeater, 'id'>;

export type EntityResponseType = HttpResponse<IRepeater>;
export type EntityArrayResponseType = HttpResponse<IRepeater[]>;

@Injectable({ providedIn: 'root' })
export class RepeaterService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/repeaters');

  create(repeater: NewRepeater): Observable<EntityResponseType> {
    return this.http.post<IRepeater>(this.resourceUrl, repeater, { observe: 'response' });
  }

  update(repeater: IRepeater): Observable<EntityResponseType> {
    return this.http.put<IRepeater>(`${this.resourceUrl}/${this.getRepeaterIdentifier(repeater)}`, repeater, { observe: 'response' });
  }

  partialUpdate(repeater: PartialUpdateRepeater): Observable<EntityResponseType> {
    return this.http.patch<IRepeater>(`${this.resourceUrl}/${this.getRepeaterIdentifier(repeater)}`, repeater, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRepeater>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRepeater[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRepeaterIdentifier(repeater: Pick<IRepeater, 'id'>): number {
    return repeater.id;
  }

  compareRepeater(o1: Pick<IRepeater, 'id'> | null, o2: Pick<IRepeater, 'id'> | null): boolean {
    return o1 && o2 ? this.getRepeaterIdentifier(o1) === this.getRepeaterIdentifier(o2) : o1 === o2;
  }

  addRepeaterToCollectionIfMissing<Type extends Pick<IRepeater, 'id'>>(
    repeaterCollection: Type[],
    ...repeatersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const repeaters: Type[] = repeatersToCheck.filter(isPresent);
    if (repeaters.length > 0) {
      const repeaterCollectionIdentifiers = repeaterCollection.map(repeaterItem => this.getRepeaterIdentifier(repeaterItem));
      const repeatersToAdd = repeaters.filter(repeaterItem => {
        const repeaterIdentifier = this.getRepeaterIdentifier(repeaterItem);
        if (repeaterCollectionIdentifiers.includes(repeaterIdentifier)) {
          return false;
        }
        repeaterCollectionIdentifiers.push(repeaterIdentifier);
        return true;
      });
      return [...repeatersToAdd, ...repeaterCollection];
    }
    return repeaterCollection;
  }
}
