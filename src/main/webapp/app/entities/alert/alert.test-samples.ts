import dayjs from 'dayjs/esm';

import { IAlert, NewAlert } from './alert.model';

export const sampleWithRequiredData: IAlert = {
  id: 32680,
  alertType: 'oof',
  description: 'fooey when',
  createdDate: dayjs('2024-06-03T13:00'),
};

export const sampleWithPartialData: IAlert = {
  id: 27963,
  alertType: 'whose veil tough',
  description: 'spattering',
  createdDate: dayjs('2024-06-02T22:55'),
};

export const sampleWithFullData: IAlert = {
  id: 20352,
  alertType: 'suspiciously',
  description: 'till',
  createdDate: dayjs('2024-06-03T01:30'),
};

export const sampleWithNewData: NewAlert = {
  alertType: 'exhausted summarise bah',
  description: 'enormously',
  createdDate: dayjs('2024-06-03T09:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
