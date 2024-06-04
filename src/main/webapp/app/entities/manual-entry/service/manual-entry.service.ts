import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManualEntry, NewManualEntry } from '../manual-entry.model';

export type PartialUpdateManualEntry = Partial<IManualEntry> & Pick<IManualEntry, 'id'>;

type RestOf<T extends IManualEntry | NewManualEntry> = Omit<T, 'entryDate'> & {
  entryDate?: string | null;
};

export type RestManualEntry = RestOf<IManualEntry>;

export type NewRestManualEntry = RestOf<NewManualEntry>;

export type PartialUpdateRestManualEntry = RestOf<PartialUpdateManualEntry>;

export type EntityResponseType = HttpResponse<IManualEntry>;
export type EntityArrayResponseType = HttpResponse<IManualEntry[]>;

@Injectable({ providedIn: 'root' })
export class ManualEntryService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/manual-entries');

  create(manualEntry: NewManualEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualEntry);
    return this.http
      .post<RestManualEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(manualEntry: IManualEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualEntry);
    return this.http
      .put<RestManualEntry>(`${this.resourceUrl}/${this.getManualEntryIdentifier(manualEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(manualEntry: PartialUpdateManualEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualEntry);
    return this.http
      .patch<RestManualEntry>(`${this.resourceUrl}/${this.getManualEntryIdentifier(manualEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestManualEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestManualEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getManualEntryIdentifier(manualEntry: Pick<IManualEntry, 'id'>): number {
    return manualEntry.id;
  }

  compareManualEntry(o1: Pick<IManualEntry, 'id'> | null, o2: Pick<IManualEntry, 'id'> | null): boolean {
    return o1 && o2 ? this.getManualEntryIdentifier(o1) === this.getManualEntryIdentifier(o2) : o1 === o2;
  }

  addManualEntryToCollectionIfMissing<Type extends Pick<IManualEntry, 'id'>>(
    manualEntryCollection: Type[],
    ...manualEntriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const manualEntries: Type[] = manualEntriesToCheck.filter(isPresent);
    if (manualEntries.length > 0) {
      const manualEntryCollectionIdentifiers = manualEntryCollection.map(manualEntryItem => this.getManualEntryIdentifier(manualEntryItem));
      const manualEntriesToAdd = manualEntries.filter(manualEntryItem => {
        const manualEntryIdentifier = this.getManualEntryIdentifier(manualEntryItem);
        if (manualEntryCollectionIdentifiers.includes(manualEntryIdentifier)) {
          return false;
        }
        manualEntryCollectionIdentifiers.push(manualEntryIdentifier);
        return true;
      });
      return [...manualEntriesToAdd, ...manualEntryCollection];
    }
    return manualEntryCollection;
  }

  protected convertDateFromClient<T extends IManualEntry | NewManualEntry | PartialUpdateManualEntry>(manualEntry: T): RestOf<T> {
    return {
      ...manualEntry,
      entryDate: manualEntry.entryDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restManualEntry: RestManualEntry): IManualEntry {
    return {
      ...restManualEntry,
      entryDate: restManualEntry.entryDate ? dayjs(restManualEntry.entryDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestManualEntry>): HttpResponse<IManualEntry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestManualEntry[]>): HttpResponse<IManualEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
