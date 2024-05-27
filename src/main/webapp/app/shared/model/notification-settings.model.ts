import { ISysUser } from 'app/shared/model/sys-user.model';

export interface INotificationSettings {
  id?: number;
  preferences?: string;
  user?: ISysUser | null;
}

export const defaultValue: Readonly<INotificationSettings> = {};
