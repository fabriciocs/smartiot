import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDadoSensor, NewDadoSensor } from '../dado-sensor.model';

export type PartialUpdateDadoSensor = Partial<IDadoSensor> & Pick<IDadoSensor, 'id'>;

type RestOf<T extends IDadoSensor | NewDadoSensor> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestDadoSensor = RestOf<IDadoSensor>;

export type NewRestDadoSensor = RestOf<NewDadoSensor>;

export type PartialUpdateRestDadoSensor = RestOf<PartialUpdateDadoSensor>;

export type EntityResponseType = HttpResponse<IDadoSensor>;
export type EntityArrayResponseType = HttpResponse<IDadoSensor[]>;

@Injectable({ providedIn: 'root' })
export class DadoSensorService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dado-sensors');

  create(dadoSensor: NewDadoSensor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dadoSensor);
    return this.http
      .post<RestDadoSensor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(dadoSensor: IDadoSensor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dadoSensor);
    return this.http
      .put<RestDadoSensor>(`${this.resourceUrl}/${this.getDadoSensorIdentifier(dadoSensor)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(dadoSensor: PartialUpdateDadoSensor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dadoSensor);
    return this.http
      .patch<RestDadoSensor>(`${this.resourceUrl}/${this.getDadoSensorIdentifier(dadoSensor)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDadoSensor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDadoSensor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDadoSensorIdentifier(dadoSensor: Pick<IDadoSensor, 'id'>): number {
    return dadoSensor.id;
  }

  compareDadoSensor(o1: Pick<IDadoSensor, 'id'> | null, o2: Pick<IDadoSensor, 'id'> | null): boolean {
    return o1 && o2 ? this.getDadoSensorIdentifier(o1) === this.getDadoSensorIdentifier(o2) : o1 === o2;
  }

  addDadoSensorToCollectionIfMissing<Type extends Pick<IDadoSensor, 'id'>>(
    dadoSensorCollection: Type[],
    ...dadoSensorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dadoSensors: Type[] = dadoSensorsToCheck.filter(isPresent);
    if (dadoSensors.length > 0) {
      const dadoSensorCollectionIdentifiers = dadoSensorCollection.map(dadoSensorItem => this.getDadoSensorIdentifier(dadoSensorItem));
      const dadoSensorsToAdd = dadoSensors.filter(dadoSensorItem => {
        const dadoSensorIdentifier = this.getDadoSensorIdentifier(dadoSensorItem);
        if (dadoSensorCollectionIdentifiers.includes(dadoSensorIdentifier)) {
          return false;
        }
        dadoSensorCollectionIdentifiers.push(dadoSensorIdentifier);
        return true;
      });
      return [...dadoSensorsToAdd, ...dadoSensorCollection];
    }
    return dadoSensorCollection;
  }

  protected convertDateFromClient<T extends IDadoSensor | NewDadoSensor | PartialUpdateDadoSensor>(dadoSensor: T): RestOf<T> {
    return {
      ...dadoSensor,
      timestamp: dadoSensor.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDadoSensor: RestDadoSensor): IDadoSensor {
    return {
      ...restDadoSensor,
      timestamp: restDadoSensor.timestamp ? dayjs(restDadoSensor.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDadoSensor>): HttpResponse<IDadoSensor> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDadoSensor[]>): HttpResponse<IDadoSensor[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
