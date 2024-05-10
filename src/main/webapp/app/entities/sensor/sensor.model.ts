import { ICliente } from 'app/entities/cliente/cliente.model';
import { IDadoSensor } from 'app/entities/dado-sensor/dado-sensor.model';
import { TipoSensor } from 'app/entities/enumerations/tipo-sensor.model';

export interface ISensor {
  id: number;
  nome?: string | null;
  tipo?: keyof typeof TipoSensor | null;
  configuracao?: string | null;
  cliente?: Pick<ICliente, 'id' | 'nome'> | null;
  dadoSensores?: Pick<IDadoSensor, 'id' | 'timestamp'> | null;
}

export type NewSensor = Omit<ISensor, 'id'> & { id: null };
