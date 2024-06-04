import dayjs from 'dayjs/esm';

import { IEnrollment, NewEnrollment } from './enrollment.model';

export const sampleWithRequiredData: IEnrollment = {
  id: 30580,
  enrollmentType: 'hm',
  enrollmentDate: dayjs('2024-06-03T04:26'),
};

export const sampleWithPartialData: IEnrollment = {
  id: 19665,
  enrollmentType: 'personal',
  enrollmentDate: dayjs('2024-06-03T10:35'),
};

export const sampleWithFullData: IEnrollment = {
  id: 12175,
  enrollmentType: 'scrunch',
  enrollmentDate: dayjs('2024-06-03T01:46'),
};

export const sampleWithNewData: NewEnrollment = {
  enrollmentType: 'bottom madly',
  enrollmentDate: dayjs('2024-06-03T14:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
