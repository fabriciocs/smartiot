export interface ISysUser {
  id: number;
  username?: string | null;
  email?: string | null;
  role?: string | null;
}

export type NewSysUser = Omit<ISysUser, 'id'> & { id: null };
