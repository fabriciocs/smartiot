import dayjs from 'dayjs';
import { ISysUser } from 'app/shared/model/sys-user.model';

export interface IReport {
  id?: number;
  title?: string;
  generatedAt?: dayjs.Dayjs;
  content?: string;
  generatedBy?: ISysUser | null;
}

export const defaultValue: Readonly<IReport> = {};
