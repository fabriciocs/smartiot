import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 20049,
  amount: 10647.56,
  paymentDate: dayjs('2024-06-03'),
};

export const sampleWithPartialData: IPayment = {
  id: 1244,
  amount: 27547.67,
  paymentDate: dayjs('2024-06-03'),
};

export const sampleWithFullData: IPayment = {
  id: 17275,
  amount: 14081.53,
  paymentDate: dayjs('2024-06-03'),
};

export const sampleWithNewData: NewPayment = {
  amount: 12599.64,
  paymentDate: dayjs('2024-06-03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
