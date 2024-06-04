import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISysUser, NewSysUser } from '../sys-user.model';

export type PartialUpdateSysUser = Partial<ISysUser> & Pick<ISysUser, 'id'>;

export type EntityResponseType = HttpResponse<ISysUser>;
export type EntityArrayResponseType = HttpResponse<ISysUser[]>;

@Injectable({ providedIn: 'root' })
export class SysUserService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sys-users');

  create(sysUser: NewSysUser): Observable<EntityResponseType> {
    return this.http.post<ISysUser>(this.resourceUrl, sysUser, { observe: 'response' });
  }

  update(sysUser: ISysUser): Observable<EntityResponseType> {
    return this.http.put<ISysUser>(`${this.resourceUrl}/${this.getSysUserIdentifier(sysUser)}`, sysUser, { observe: 'response' });
  }

  partialUpdate(sysUser: PartialUpdateSysUser): Observable<EntityResponseType> {
    return this.http.patch<ISysUser>(`${this.resourceUrl}/${this.getSysUserIdentifier(sysUser)}`, sysUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISysUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISysUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSysUserIdentifier(sysUser: Pick<ISysUser, 'id'>): number {
    return sysUser.id;
  }

  compareSysUser(o1: Pick<ISysUser, 'id'> | null, o2: Pick<ISysUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getSysUserIdentifier(o1) === this.getSysUserIdentifier(o2) : o1 === o2;
  }

  addSysUserToCollectionIfMissing<Type extends Pick<ISysUser, 'id'>>(
    sysUserCollection: Type[],
    ...sysUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sysUsers: Type[] = sysUsersToCheck.filter(isPresent);
    if (sysUsers.length > 0) {
      const sysUserCollectionIdentifiers = sysUserCollection.map(sysUserItem => this.getSysUserIdentifier(sysUserItem));
      const sysUsersToAdd = sysUsers.filter(sysUserItem => {
        const sysUserIdentifier = this.getSysUserIdentifier(sysUserItem);
        if (sysUserCollectionIdentifiers.includes(sysUserIdentifier)) {
          return false;
        }
        sysUserCollectionIdentifiers.push(sysUserIdentifier);
        return true;
      });
      return [...sysUsersToAdd, ...sysUserCollection];
    }
    return sysUserCollection;
  }
}
