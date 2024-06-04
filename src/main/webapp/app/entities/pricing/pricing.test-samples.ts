import { IPricing, NewPricing } from './pricing.model';

export const sampleWithRequiredData: IPricing = {
  id: 7987,
  serviceType: 'yew till meh',
  price: 24648.37,
};

export const sampleWithPartialData: IPricing = {
  id: 5883,
  serviceType: 'when where meanwhile',
  price: 4247.71,
};

export const sampleWithFullData: IPricing = {
  id: 1709,
  serviceType: 'thoroughly softly yowza',
  price: 13614.99,
};

export const sampleWithNewData: NewPricing = {
  serviceType: 'rigidly',
  price: 30931.35,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
