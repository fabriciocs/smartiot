import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeasurement, NewMeasurement } from '../measurement.model';

export type PartialUpdateMeasurement = Partial<IMeasurement> & Pick<IMeasurement, 'id'>;

type RestOf<T extends IMeasurement | NewMeasurement> = Omit<T, 'measurementTime'> & {
  measurementTime?: string | null;
};

export type RestMeasurement = RestOf<IMeasurement>;

export type NewRestMeasurement = RestOf<NewMeasurement>;

export type PartialUpdateRestMeasurement = RestOf<PartialUpdateMeasurement>;

export type EntityResponseType = HttpResponse<IMeasurement>;
export type EntityArrayResponseType = HttpResponse<IMeasurement[]>;

@Injectable({ providedIn: 'root' })
export class MeasurementService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/measurements');

  create(measurement: NewMeasurement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(measurement);
    return this.http
      .post<RestMeasurement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(measurement: IMeasurement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(measurement);
    return this.http
      .put<RestMeasurement>(`${this.resourceUrl}/${this.getMeasurementIdentifier(measurement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(measurement: PartialUpdateMeasurement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(measurement);
    return this.http
      .patch<RestMeasurement>(`${this.resourceUrl}/${this.getMeasurementIdentifier(measurement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMeasurement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMeasurement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMeasurementIdentifier(measurement: Pick<IMeasurement, 'id'>): number {
    return measurement.id;
  }

  compareMeasurement(o1: Pick<IMeasurement, 'id'> | null, o2: Pick<IMeasurement, 'id'> | null): boolean {
    return o1 && o2 ? this.getMeasurementIdentifier(o1) === this.getMeasurementIdentifier(o2) : o1 === o2;
  }

  addMeasurementToCollectionIfMissing<Type extends Pick<IMeasurement, 'id'>>(
    measurementCollection: Type[],
    ...measurementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const measurements: Type[] = measurementsToCheck.filter(isPresent);
    if (measurements.length > 0) {
      const measurementCollectionIdentifiers = measurementCollection.map(measurementItem => this.getMeasurementIdentifier(measurementItem));
      const measurementsToAdd = measurements.filter(measurementItem => {
        const measurementIdentifier = this.getMeasurementIdentifier(measurementItem);
        if (measurementCollectionIdentifiers.includes(measurementIdentifier)) {
          return false;
        }
        measurementCollectionIdentifiers.push(measurementIdentifier);
        return true;
      });
      return [...measurementsToAdd, ...measurementCollection];
    }
    return measurementCollection;
  }

  protected convertDateFromClient<T extends IMeasurement | NewMeasurement | PartialUpdateMeasurement>(measurement: T): RestOf<T> {
    return {
      ...measurement,
      measurementTime: measurement.measurementTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMeasurement: RestMeasurement): IMeasurement {
    return {
      ...restMeasurement,
      measurementTime: restMeasurement.measurementTime ? dayjs(restMeasurement.measurementTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMeasurement>): HttpResponse<IMeasurement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMeasurement[]>): HttpResponse<IMeasurement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
