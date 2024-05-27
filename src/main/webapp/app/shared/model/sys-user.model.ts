import dayjs from 'dayjs';
import { ISysRole } from 'app/shared/model/sys-role.model';

export interface ISysUser {
  id?: number;
  name?: string;
  email?: string;
  passwordHash?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  role?: ISysRole | null;
}

export const defaultValue: Readonly<ISysUser> = {};
