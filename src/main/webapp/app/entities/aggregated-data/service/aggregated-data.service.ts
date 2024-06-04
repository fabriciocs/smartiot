import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAggregatedData, NewAggregatedData } from '../aggregated-data.model';

export type PartialUpdateAggregatedData = Partial<IAggregatedData> & Pick<IAggregatedData, 'id'>;

type RestOf<T extends IAggregatedData | NewAggregatedData> = Omit<T, 'aggregationTime'> & {
  aggregationTime?: string | null;
};

export type RestAggregatedData = RestOf<IAggregatedData>;

export type NewRestAggregatedData = RestOf<NewAggregatedData>;

export type PartialUpdateRestAggregatedData = RestOf<PartialUpdateAggregatedData>;

export type EntityResponseType = HttpResponse<IAggregatedData>;
export type EntityArrayResponseType = HttpResponse<IAggregatedData[]>;

@Injectable({ providedIn: 'root' })
export class AggregatedDataService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aggregated-data');

  create(aggregatedData: NewAggregatedData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aggregatedData);
    return this.http
      .post<RestAggregatedData>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aggregatedData: IAggregatedData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aggregatedData);
    return this.http
      .put<RestAggregatedData>(`${this.resourceUrl}/${this.getAggregatedDataIdentifier(aggregatedData)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aggregatedData: PartialUpdateAggregatedData): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aggregatedData);
    return this.http
      .patch<RestAggregatedData>(`${this.resourceUrl}/${this.getAggregatedDataIdentifier(aggregatedData)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAggregatedData>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAggregatedData[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAggregatedDataIdentifier(aggregatedData: Pick<IAggregatedData, 'id'>): number {
    return aggregatedData.id;
  }

  compareAggregatedData(o1: Pick<IAggregatedData, 'id'> | null, o2: Pick<IAggregatedData, 'id'> | null): boolean {
    return o1 && o2 ? this.getAggregatedDataIdentifier(o1) === this.getAggregatedDataIdentifier(o2) : o1 === o2;
  }

  addAggregatedDataToCollectionIfMissing<Type extends Pick<IAggregatedData, 'id'>>(
    aggregatedDataCollection: Type[],
    ...aggregatedDataToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aggregatedData: Type[] = aggregatedDataToCheck.filter(isPresent);
    if (aggregatedData.length > 0) {
      const aggregatedDataCollectionIdentifiers = aggregatedDataCollection.map(aggregatedDataItem =>
        this.getAggregatedDataIdentifier(aggregatedDataItem),
      );
      const aggregatedDataToAdd = aggregatedData.filter(aggregatedDataItem => {
        const aggregatedDataIdentifier = this.getAggregatedDataIdentifier(aggregatedDataItem);
        if (aggregatedDataCollectionIdentifiers.includes(aggregatedDataIdentifier)) {
          return false;
        }
        aggregatedDataCollectionIdentifiers.push(aggregatedDataIdentifier);
        return true;
      });
      return [...aggregatedDataToAdd, ...aggregatedDataCollection];
    }
    return aggregatedDataCollection;
  }

  protected convertDateFromClient<T extends IAggregatedData | NewAggregatedData | PartialUpdateAggregatedData>(
    aggregatedData: T,
  ): RestOf<T> {
    return {
      ...aggregatedData,
      aggregationTime: aggregatedData.aggregationTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAggregatedData: RestAggregatedData): IAggregatedData {
    return {
      ...restAggregatedData,
      aggregationTime: restAggregatedData.aggregationTime ? dayjs(restAggregatedData.aggregationTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAggregatedData>): HttpResponse<IAggregatedData> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAggregatedData[]>): HttpResponse<IAggregatedData[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
