import dayjs from 'dayjs/esm';

import { IDadoSensor, NewDadoSensor } from './dado-sensor.model';

export const sampleWithRequiredData: IDadoSensor = {
  id: 22259,
};

export const sampleWithPartialData: IDadoSensor = {
  id: 13212,
  dados: 'vilify before',
};

export const sampleWithFullData: IDadoSensor = {
  id: 14005,
  dados: 'from',
  timestamp: dayjs('2024-05-09T08:02'),
};

export const sampleWithNewData: NewDadoSensor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
