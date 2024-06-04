import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 25708,
  notificationType: 'left supposing why',
  message: 'rundown along',
  sentDate: dayjs('2024-06-03T15:41'),
};

export const sampleWithPartialData: INotification = {
  id: 20661,
  notificationType: 'before quicker',
  message: 'gah electric yippee',
  sentDate: dayjs('2024-06-03T03:17'),
};

export const sampleWithFullData: INotification = {
  id: 4335,
  notificationType: 'how',
  message: 'openly',
  sentDate: dayjs('2024-06-03T06:24'),
};

export const sampleWithNewData: NewNotification = {
  notificationType: 'once sombrero beside',
  message: 'beyond whereas',
  sentDate: dayjs('2024-06-03T13:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
