import { IResourceGroup, NewResourceGroup } from './resource-group.model';

export const sampleWithRequiredData: IResourceGroup = {
  id: 27731,
  name: 'besides',
  description: 'click',
};

export const sampleWithPartialData: IResourceGroup = {
  id: 11128,
  name: 'whirlpool merry',
  description: 'beside',
};

export const sampleWithFullData: IResourceGroup = {
  id: 25970,
  name: 'meanwhile pessimistic above',
  description: 'but as',
};

export const sampleWithNewData: NewResourceGroup = {
  name: 'firsthand',
  description: 'wonderfully exasperate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
