import dayjs from 'dayjs/esm';

import { IMeasurement, NewMeasurement } from './measurement.model';

export const sampleWithRequiredData: IMeasurement = {
  id: 7408,
  measurementType: 'consequently forenenst tile',
  value: 'since gadzooks',
  measurementTime: dayjs('2024-06-03T14:38'),
};

export const sampleWithPartialData: IMeasurement = {
  id: 17668,
  measurementType: 'except back yahoo',
  value: 'nurture arid',
  measurementTime: dayjs('2024-06-03T00:59'),
};

export const sampleWithFullData: IMeasurement = {
  id: 19896,
  measurementType: 'lest',
  value: 'abandoned playfully',
  measurementTime: dayjs('2024-06-03T09:05'),
};

export const sampleWithNewData: NewMeasurement = {
  measurementType: 'concerning',
  value: 'dutiful athwart',
  measurementTime: dayjs('2024-06-03T15:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
