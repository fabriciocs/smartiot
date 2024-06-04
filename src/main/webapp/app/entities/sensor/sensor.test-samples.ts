import { ISensor, NewSensor } from './sensor.model';

export const sampleWithRequiredData: ISensor = {
  id: 6214,
  nome: 'hoon display over',
  tipo: 'PRESSURE',
};

export const sampleWithPartialData: ISensor = {
  id: 21007,
  nome: 'amidst overrate oh',
  tipo: 'PRESSURE',
};

export const sampleWithFullData: ISensor = {
  id: 20268,
  nome: 'false livid',
  tipo: 'HUMIDITY',
  configuracao: 'at although about',
};

export const sampleWithNewData: NewSensor = {
  nome: 'scorn common',
  tipo: 'TEMPERATURE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
