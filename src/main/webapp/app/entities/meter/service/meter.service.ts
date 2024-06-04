import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeter, NewMeter } from '../meter.model';

export type PartialUpdateMeter = Partial<IMeter> & Pick<IMeter, 'id'>;

export type EntityResponseType = HttpResponse<IMeter>;
export type EntityArrayResponseType = HttpResponse<IMeter[]>;

@Injectable({ providedIn: 'root' })
export class MeterService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/meters');

  create(meter: NewMeter): Observable<EntityResponseType> {
    return this.http.post<IMeter>(this.resourceUrl, meter, { observe: 'response' });
  }

  update(meter: IMeter): Observable<EntityResponseType> {
    return this.http.put<IMeter>(`${this.resourceUrl}/${this.getMeterIdentifier(meter)}`, meter, { observe: 'response' });
  }

  partialUpdate(meter: PartialUpdateMeter): Observable<EntityResponseType> {
    return this.http.patch<IMeter>(`${this.resourceUrl}/${this.getMeterIdentifier(meter)}`, meter, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMeter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMeterIdentifier(meter: Pick<IMeter, 'id'>): number {
    return meter.id;
  }

  compareMeter(o1: Pick<IMeter, 'id'> | null, o2: Pick<IMeter, 'id'> | null): boolean {
    return o1 && o2 ? this.getMeterIdentifier(o1) === this.getMeterIdentifier(o2) : o1 === o2;
  }

  addMeterToCollectionIfMissing<Type extends Pick<IMeter, 'id'>>(
    meterCollection: Type[],
    ...metersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const meters: Type[] = metersToCheck.filter(isPresent);
    if (meters.length > 0) {
      const meterCollectionIdentifiers = meterCollection.map(meterItem => this.getMeterIdentifier(meterItem));
      const metersToAdd = meters.filter(meterItem => {
        const meterIdentifier = this.getMeterIdentifier(meterItem);
        if (meterCollectionIdentifiers.includes(meterIdentifier)) {
          return false;
        }
        meterCollectionIdentifiers.push(meterIdentifier);
        return true;
      });
      return [...metersToAdd, ...meterCollection];
    }
    return meterCollection;
  }
}
