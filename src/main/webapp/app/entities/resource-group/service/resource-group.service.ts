import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResourceGroup, NewResourceGroup } from '../resource-group.model';

export type PartialUpdateResourceGroup = Partial<IResourceGroup> & Pick<IResourceGroup, 'id'>;

export type EntityResponseType = HttpResponse<IResourceGroup>;
export type EntityArrayResponseType = HttpResponse<IResourceGroup[]>;

@Injectable({ providedIn: 'root' })
export class ResourceGroupService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resource-groups');

  create(resourceGroup: NewResourceGroup): Observable<EntityResponseType> {
    return this.http.post<IResourceGroup>(this.resourceUrl, resourceGroup, { observe: 'response' });
  }

  update(resourceGroup: IResourceGroup): Observable<EntityResponseType> {
    return this.http.put<IResourceGroup>(`${this.resourceUrl}/${this.getResourceGroupIdentifier(resourceGroup)}`, resourceGroup, {
      observe: 'response',
    });
  }

  partialUpdate(resourceGroup: PartialUpdateResourceGroup): Observable<EntityResponseType> {
    return this.http.patch<IResourceGroup>(`${this.resourceUrl}/${this.getResourceGroupIdentifier(resourceGroup)}`, resourceGroup, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResourceGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResourceGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getResourceGroupIdentifier(resourceGroup: Pick<IResourceGroup, 'id'>): number {
    return resourceGroup.id;
  }

  compareResourceGroup(o1: Pick<IResourceGroup, 'id'> | null, o2: Pick<IResourceGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getResourceGroupIdentifier(o1) === this.getResourceGroupIdentifier(o2) : o1 === o2;
  }

  addResourceGroupToCollectionIfMissing<Type extends Pick<IResourceGroup, 'id'>>(
    resourceGroupCollection: Type[],
    ...resourceGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const resourceGroups: Type[] = resourceGroupsToCheck.filter(isPresent);
    if (resourceGroups.length > 0) {
      const resourceGroupCollectionIdentifiers = resourceGroupCollection.map(resourceGroupItem =>
        this.getResourceGroupIdentifier(resourceGroupItem),
      );
      const resourceGroupsToAdd = resourceGroups.filter(resourceGroupItem => {
        const resourceGroupIdentifier = this.getResourceGroupIdentifier(resourceGroupItem);
        if (resourceGroupCollectionIdentifiers.includes(resourceGroupIdentifier)) {
          return false;
        }
        resourceGroupCollectionIdentifiers.push(resourceGroupIdentifier);
        return true;
      });
      return [...resourceGroupsToAdd, ...resourceGroupCollection];
    }
    return resourceGroupCollection;
  }
}
