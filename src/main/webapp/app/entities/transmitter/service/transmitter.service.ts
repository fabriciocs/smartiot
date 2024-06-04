import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransmitter, NewTransmitter } from '../transmitter.model';

export type PartialUpdateTransmitter = Partial<ITransmitter> & Pick<ITransmitter, 'id'>;

export type EntityResponseType = HttpResponse<ITransmitter>;
export type EntityArrayResponseType = HttpResponse<ITransmitter[]>;

@Injectable({ providedIn: 'root' })
export class TransmitterService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transmitters');

  create(transmitter: NewTransmitter): Observable<EntityResponseType> {
    return this.http.post<ITransmitter>(this.resourceUrl, transmitter, { observe: 'response' });
  }

  update(transmitter: ITransmitter): Observable<EntityResponseType> {
    return this.http.put<ITransmitter>(`${this.resourceUrl}/${this.getTransmitterIdentifier(transmitter)}`, transmitter, {
      observe: 'response',
    });
  }

  partialUpdate(transmitter: PartialUpdateTransmitter): Observable<EntityResponseType> {
    return this.http.patch<ITransmitter>(`${this.resourceUrl}/${this.getTransmitterIdentifier(transmitter)}`, transmitter, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransmitter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransmitter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTransmitterIdentifier(transmitter: Pick<ITransmitter, 'id'>): number {
    return transmitter.id;
  }

  compareTransmitter(o1: Pick<ITransmitter, 'id'> | null, o2: Pick<ITransmitter, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransmitterIdentifier(o1) === this.getTransmitterIdentifier(o2) : o1 === o2;
  }

  addTransmitterToCollectionIfMissing<Type extends Pick<ITransmitter, 'id'>>(
    transmitterCollection: Type[],
    ...transmittersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transmitters: Type[] = transmittersToCheck.filter(isPresent);
    if (transmitters.length > 0) {
      const transmitterCollectionIdentifiers = transmitterCollection.map(transmitterItem => this.getTransmitterIdentifier(transmitterItem));
      const transmittersToAdd = transmitters.filter(transmitterItem => {
        const transmitterIdentifier = this.getTransmitterIdentifier(transmitterItem);
        if (transmitterCollectionIdentifiers.includes(transmitterIdentifier)) {
          return false;
        }
        transmitterCollectionIdentifiers.push(transmitterIdentifier);
        return true;
      });
      return [...transmittersToAdd, ...transmitterCollection];
    }
    return transmitterCollection;
  }
}
