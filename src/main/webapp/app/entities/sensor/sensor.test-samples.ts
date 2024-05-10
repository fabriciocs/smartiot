import { ISensor, NewSensor } from './sensor.model';

export const sampleWithRequiredData: ISensor = {
  id: 25971,
  nome: 'upbeat memorize excerpt',
  tipo: 'PRESSURE',
};

export const sampleWithPartialData: ISensor = {
  id: 8761,
  nome: 'recess first notwithstanding',
  tipo: 'TEMPERATURE',
};

export const sampleWithFullData: ISensor = {
  id: 4688,
  nome: 'adorable',
  tipo: 'TEMPERATURE',
  configuracao: 'chops dislocate',
};

export const sampleWithNewData: NewSensor = {
  nome: 'than fast widow',
  tipo: 'HUMIDITY',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
