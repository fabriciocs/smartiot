import dayjs from 'dayjs';

export interface IDadoSensor {
  id?: number;
  dados?: string | null;
  timestamp?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IDadoSensor> = {};
