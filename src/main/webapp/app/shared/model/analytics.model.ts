import dayjs from 'dayjs';

export interface IAnalytics {
  id?: number;
  type?: string;
  data?: string;
  createdAt?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IAnalytics> = {};
