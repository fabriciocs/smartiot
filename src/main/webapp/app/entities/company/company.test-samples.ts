import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 9681,
  name: 'duster',
  address: 'polymerise consul prise',
};

export const sampleWithPartialData: ICompany = {
  id: 12250,
  name: 'ruler boo',
  address: 'shallow',
};

export const sampleWithFullData: ICompany = {
  id: 9155,
  name: 'around lest now',
  address: 'meridian query descend',
};

export const sampleWithNewData: NewCompany = {
  name: 'gah',
  address: 'beneath as encirclement',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
