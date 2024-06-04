import dayjs from 'dayjs/esm';

import { IDadoSensor, NewDadoSensor } from './dado-sensor.model';

export const sampleWithRequiredData: IDadoSensor = {
  id: 16793,
};

export const sampleWithPartialData: IDadoSensor = {
  id: 2092,
  dados: 'good once yuck',
  timestamp: dayjs('2024-05-09T01:53'),
};

export const sampleWithFullData: IDadoSensor = {
  id: 1425,
  dados: 'until',
  timestamp: dayjs('2024-05-09T08:13'),
};

export const sampleWithNewData: NewDadoSensor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
