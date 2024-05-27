import dayjs from 'dayjs';
import { ISysUser } from 'app/shared/model/sys-user.model';

export interface INotification {
  id?: number;
  type?: string;
  message?: string;
  status?: string;
  createdAt?: dayjs.Dayjs;
  recipient?: ISysUser | null;
}

export const defaultValue: Readonly<INotification> = {};
