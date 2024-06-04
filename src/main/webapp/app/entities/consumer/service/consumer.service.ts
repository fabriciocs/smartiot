import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsumer, NewConsumer } from '../consumer.model';

export type PartialUpdateConsumer = Partial<IConsumer> & Pick<IConsumer, 'id'>;

export type EntityResponseType = HttpResponse<IConsumer>;
export type EntityArrayResponseType = HttpResponse<IConsumer[]>;

@Injectable({ providedIn: 'root' })
export class ConsumerService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consumers');

  create(consumer: NewConsumer): Observable<EntityResponseType> {
    return this.http.post<IConsumer>(this.resourceUrl, consumer, { observe: 'response' });
  }

  update(consumer: IConsumer): Observable<EntityResponseType> {
    return this.http.put<IConsumer>(`${this.resourceUrl}/${this.getConsumerIdentifier(consumer)}`, consumer, { observe: 'response' });
  }

  partialUpdate(consumer: PartialUpdateConsumer): Observable<EntityResponseType> {
    return this.http.patch<IConsumer>(`${this.resourceUrl}/${this.getConsumerIdentifier(consumer)}`, consumer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConsumer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConsumer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConsumerIdentifier(consumer: Pick<IConsumer, 'id'>): number {
    return consumer.id;
  }

  compareConsumer(o1: Pick<IConsumer, 'id'> | null, o2: Pick<IConsumer, 'id'> | null): boolean {
    return o1 && o2 ? this.getConsumerIdentifier(o1) === this.getConsumerIdentifier(o2) : o1 === o2;
  }

  addConsumerToCollectionIfMissing<Type extends Pick<IConsumer, 'id'>>(
    consumerCollection: Type[],
    ...consumersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const consumers: Type[] = consumersToCheck.filter(isPresent);
    if (consumers.length > 0) {
      const consumerCollectionIdentifiers = consumerCollection.map(consumerItem => this.getConsumerIdentifier(consumerItem));
      const consumersToAdd = consumers.filter(consumerItem => {
        const consumerIdentifier = this.getConsumerIdentifier(consumerItem);
        if (consumerCollectionIdentifiers.includes(consumerIdentifier)) {
          return false;
        }
        consumerCollectionIdentifiers.push(consumerIdentifier);
        return true;
      });
      return [...consumersToAdd, ...consumerCollection];
    }
    return consumerCollection;
  }
}
