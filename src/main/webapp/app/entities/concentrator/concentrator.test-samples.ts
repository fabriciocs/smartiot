import { IConcentrator, NewConcentrator } from './concentrator.model';

export const sampleWithRequiredData: IConcentrator = {
  id: 17220,
  serialNumber: 'until traffic',
  capacity: 7475,
};

export const sampleWithPartialData: IConcentrator = {
  id: 7318,
  serialNumber: 'partial',
  capacity: 30796,
};

export const sampleWithFullData: IConcentrator = {
  id: 23121,
  serialNumber: 'engagement helplessly',
  capacity: 23418,
};

export const sampleWithNewData: NewConcentrator = {
  serialNumber: 'overlap',
  capacity: 1695,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
