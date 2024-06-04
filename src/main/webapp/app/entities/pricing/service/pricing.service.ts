import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPricing, NewPricing } from '../pricing.model';

export type PartialUpdatePricing = Partial<IPricing> & Pick<IPricing, 'id'>;

export type EntityResponseType = HttpResponse<IPricing>;
export type EntityArrayResponseType = HttpResponse<IPricing[]>;

@Injectable({ providedIn: 'root' })
export class PricingService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pricings');

  create(pricing: NewPricing): Observable<EntityResponseType> {
    return this.http.post<IPricing>(this.resourceUrl, pricing, { observe: 'response' });
  }

  update(pricing: IPricing): Observable<EntityResponseType> {
    return this.http.put<IPricing>(`${this.resourceUrl}/${this.getPricingIdentifier(pricing)}`, pricing, { observe: 'response' });
  }

  partialUpdate(pricing: PartialUpdatePricing): Observable<EntityResponseType> {
    return this.http.patch<IPricing>(`${this.resourceUrl}/${this.getPricingIdentifier(pricing)}`, pricing, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPricing>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPricing[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPricingIdentifier(pricing: Pick<IPricing, 'id'>): number {
    return pricing.id;
  }

  comparePricing(o1: Pick<IPricing, 'id'> | null, o2: Pick<IPricing, 'id'> | null): boolean {
    return o1 && o2 ? this.getPricingIdentifier(o1) === this.getPricingIdentifier(o2) : o1 === o2;
  }

  addPricingToCollectionIfMissing<Type extends Pick<IPricing, 'id'>>(
    pricingCollection: Type[],
    ...pricingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pricings: Type[] = pricingsToCheck.filter(isPresent);
    if (pricings.length > 0) {
      const pricingCollectionIdentifiers = pricingCollection.map(pricingItem => this.getPricingIdentifier(pricingItem));
      const pricingsToAdd = pricings.filter(pricingItem => {
        const pricingIdentifier = this.getPricingIdentifier(pricingItem);
        if (pricingCollectionIdentifiers.includes(pricingIdentifier)) {
          return false;
        }
        pricingCollectionIdentifiers.push(pricingIdentifier);
        return true;
      });
      return [...pricingsToAdd, ...pricingCollection];
    }
    return pricingCollection;
  }
}
