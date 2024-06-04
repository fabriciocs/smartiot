import dayjs from 'dayjs/esm';
import { IEnrollment } from 'app/entities/enrollment/enrollment.model';

export interface IMeasurement {
  id: number;
  measurementType?: string | null;
  value?: string | null;
  measurementTime?: dayjs.Dayjs | null;
  enrollment?: Pick<IEnrollment, 'id'> | null;
}

export type NewMeasurement = Omit<IMeasurement, 'id'> & { id: null };
