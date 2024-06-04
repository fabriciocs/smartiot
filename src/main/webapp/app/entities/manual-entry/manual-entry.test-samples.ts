import dayjs from 'dayjs/esm';

import { IManualEntry, NewManualEntry } from './manual-entry.model';

export const sampleWithRequiredData: IManualEntry = {
  id: 3522,
  entryType: 'wiretap vigorous',
  value: 'eek',
  entryDate: dayjs('2024-06-03T14:53'),
};

export const sampleWithPartialData: IManualEntry = {
  id: 7920,
  entryType: 'oof while',
  value: 'until meh along',
  entryDate: dayjs('2024-06-03T05:42'),
};

export const sampleWithFullData: IManualEntry = {
  id: 23855,
  entryType: 'opposite faithfully scarily',
  value: 'delightfully yellow',
  entryDate: dayjs('2024-06-03T13:08'),
};

export const sampleWithNewData: NewManualEntry = {
  entryType: 'without grease',
  value: 'dawdle for',
  entryDate: dayjs('2024-06-03T16:04'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
