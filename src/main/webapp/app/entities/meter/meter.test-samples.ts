import { IMeter, NewMeter } from './meter.model';

export const sampleWithRequiredData: IMeter = {
  id: 19103,
  serialNumber: 'daintily unaccountably',
  location: 'aspect yet',
};

export const sampleWithPartialData: IMeter = {
  id: 32213,
  serialNumber: 'provided astride',
  location: 'cloudy',
};

export const sampleWithFullData: IMeter = {
  id: 6134,
  serialNumber: 'farewell beyond',
  location: 'wee cover',
};

export const sampleWithNewData: NewMeter = {
  serialNumber: 'offensively or',
  location: 'mention key',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
