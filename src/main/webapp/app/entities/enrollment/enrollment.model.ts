import dayjs from 'dayjs/esm';

export interface IEnrollment {
  id: number;
  enrollmentType?: string | null;
  enrollmentDate?: dayjs.Dayjs | null;
}

export type NewEnrollment = Omit<IEnrollment, 'id'> & { id: null };
