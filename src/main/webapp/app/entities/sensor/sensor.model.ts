import { IConfiguracaoAlerta } from 'app/entities/configuracao-alerta/configuracao-alerta.model';
import { IDadoSensor } from 'app/entities/dado-sensor/dado-sensor.model';
import { TipoSensor } from 'app/entities/enumerations/tipo-sensor.model';

export interface ISensor {
  id: number;
  nome?: string | null;
  tipo?: keyof typeof TipoSensor | null;
  configuracao?: string | null;
  configuracaoAlertas?: Pick<IConfiguracaoAlerta, 'id'> | null;
  dadoSensores?: Pick<IDadoSensor, 'id'> | null;
}

export type NewSensor = Omit<ISensor, 'id'> & { id: null };
