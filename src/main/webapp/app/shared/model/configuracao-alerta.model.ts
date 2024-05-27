import { ISensor } from 'app/shared/model/sensor.model';

export interface IConfiguracaoAlerta {
  id?: number;
  limite?: number | null;
  email?: string;
  sensor?: ISensor;
}

export const defaultValue: Readonly<IConfiguracaoAlerta> = {};
