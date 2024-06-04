import { ITransmitter, NewTransmitter } from './transmitter.model';

export const sampleWithRequiredData: ITransmitter = {
  id: 14470,
  serialNumber: 'spicy perspire',
  frequency: 5274,
};

export const sampleWithPartialData: ITransmitter = {
  id: 27415,
  serialNumber: 'eek provided',
  frequency: 5467,
};

export const sampleWithFullData: ITransmitter = {
  id: 32301,
  serialNumber: 'lady troubled gee',
  frequency: 30422,
};

export const sampleWithNewData: NewTransmitter = {
  serialNumber: 'phew',
  frequency: 27724,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
