import { ICostCenter, NewCostCenter } from './cost-center.model';

export const sampleWithRequiredData: ICostCenter = {
  id: 15219,
  name: 'burden',
  budget: 20834.94,
};

export const sampleWithPartialData: ICostCenter = {
  id: 16747,
  name: 'crawdad',
  budget: 25479.2,
};

export const sampleWithFullData: ICostCenter = {
  id: 7660,
  name: 'serene um',
  budget: 11208.91,
};

export const sampleWithNewData: NewCostCenter = {
  name: 'amidst',
  budget: 21203.61,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
