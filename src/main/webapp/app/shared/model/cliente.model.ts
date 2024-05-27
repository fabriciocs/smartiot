export interface ICliente {
  id?: number;
  nome?: string;
  email?: string;
}

export const defaultValue: Readonly<ICliente> = {};
