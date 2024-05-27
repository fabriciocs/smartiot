import { ISysUser } from 'app/shared/model/sys-user.model';

export interface IProfile {
  id?: number;
  phoneNumber?: string | null;
  address?: string | null;
  profilePicture?: string | null;
  preferences?: string | null;
  user?: ISysUser | null;
}

export const defaultValue: Readonly<IProfile> = {};
