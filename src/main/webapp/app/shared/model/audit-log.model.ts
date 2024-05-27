import dayjs from 'dayjs';
import { ISysUser } from 'app/shared/model/sys-user.model';

export interface IAuditLog {
  id?: number;
  action?: string;
  timestamp?: dayjs.Dayjs;
  details?: string | null;
  user?: ISysUser | null;
}

export const defaultValue: Readonly<IAuditLog> = {};
