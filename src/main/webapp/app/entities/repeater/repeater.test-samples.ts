import { IRepeater, NewRepeater } from './repeater.model';

export const sampleWithRequiredData: IRepeater = {
  id: 10834,
  serialNumber: 'exhume',
  range: 2078,
};

export const sampleWithPartialData: IRepeater = {
  id: 17678,
  serialNumber: 'ick jealous misinform',
  range: 8716,
};

export const sampleWithFullData: IRepeater = {
  id: 26257,
  serialNumber: 'ew',
  range: 15909,
};

export const sampleWithNewData: NewRepeater = {
  serialNumber: 'thesis undercharge quote',
  range: 29158,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
