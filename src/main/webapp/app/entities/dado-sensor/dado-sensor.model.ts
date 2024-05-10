import dayjs from 'dayjs/esm';

export interface IDadoSensor {
  id: number;
  dados?: string | null;
  timestamp?: dayjs.Dayjs | null;
}

export type NewDadoSensor = Omit<IDadoSensor, 'id'> & { id: null };
