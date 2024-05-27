import { ICliente } from 'app/shared/model/cliente.model';
import { IDadoSensor } from 'app/shared/model/dado-sensor.model';
import { TipoSensor } from 'app/shared/model/enumerations/tipo-sensor.model';

export interface ISensor {
  id?: number;
  nome?: string;
  tipo?: keyof typeof TipoSensor;
  configuracao?: string | null;
  cliente?: ICliente;
  dadoSensores?: IDadoSensor | null;
}

export const defaultValue: Readonly<ISensor> = {};
