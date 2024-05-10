import { ISensor } from 'app/entities/sensor/sensor.model';

export interface ICliente {
  id: number;
  nome?: string | null;
  email?: string | null;
  sensores?: Pick<ISensor, 'id'> | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
