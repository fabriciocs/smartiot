import { ISensor } from 'app/entities/sensor/sensor.model';

export interface IConfiguracaoAlerta {
  id: number;
  limite?: number | null;
  email?: string | null;
  sensor?: Pick<ISensor, 'id' | 'nome'> | null;
}

export type NewConfiguracaoAlerta = Omit<IConfiguracaoAlerta, 'id'> & { id: null };
