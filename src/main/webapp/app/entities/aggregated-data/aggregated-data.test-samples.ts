import dayjs from 'dayjs/esm';

import { IAggregatedData, NewAggregatedData } from './aggregated-data.model';

export const sampleWithRequiredData: IAggregatedData = {
  id: 14126,
  dataType: 'if evangelise quality',
  value: 'daring',
  aggregationTime: dayjs('2024-06-02T22:11'),
};

export const sampleWithPartialData: IAggregatedData = {
  id: 31165,
  dataType: 'boohoo',
  value: 'disgorge assistant',
  aggregationTime: dayjs('2024-06-03T06:26'),
};

export const sampleWithFullData: IAggregatedData = {
  id: 81,
  dataType: 'geez upright kindly',
  value: 'fancy mysteriously until',
  aggregationTime: dayjs('2024-06-02T23:33'),
};

export const sampleWithNewData: NewAggregatedData = {
  dataType: 'gosh woot vacantly',
  value: 'hence pence',
  aggregationTime: dayjs('2024-06-03T17:25'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
