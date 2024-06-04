import dayjs from 'dayjs/esm';

export interface INotification {
  id: number;
  notificationType?: string | null;
  message?: string | null;
  sentDate?: dayjs.Dayjs | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
