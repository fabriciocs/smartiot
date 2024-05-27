import dayjs from 'dayjs';
import { ISysUser } from 'app/shared/model/sys-user.model';

export interface IFeedbackForm {
  id?: number;
  title?: string;
  description?: string | null;
  status?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  creator?: ISysUser | null;
}

export const defaultValue: Readonly<IFeedbackForm> = {};
