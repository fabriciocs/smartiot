import { ISensor, NewSensor } from './sensor.model';

export const sampleWithRequiredData: ISensor = {
  id: 3966,
  nome: 'after readily',
  tipo: 'TEMPERATURE',
};

export const sampleWithPartialData: ISensor = {
  id: 17229,
  nome: 'till',
  tipo: 'HUMIDITY',
};

export const sampleWithFullData: ISensor = {
  id: 8761,
  nome: 'recess first notwithstanding',
  tipo: 'TEMPERATURE',
  configuracao: 'zowie',
};

export const sampleWithNewData: NewSensor = {
  nome: 'versus',
  tipo: 'PRESSURE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
