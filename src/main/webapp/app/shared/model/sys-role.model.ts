export interface ISysRole {
  id?: number;
  roleName?: string;
  description?: string | null;
}

export const defaultValue: Readonly<ISysRole> = {};
