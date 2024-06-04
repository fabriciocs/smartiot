import dayjs from 'dayjs/esm';
import { IConsumer } from 'app/entities/consumer/consumer.model';

export interface IAlert {
  id: number;
  alertType?: string | null;
  description?: string | null;
  createdDate?: dayjs.Dayjs | null;
  consumer?: Pick<IConsumer, 'id'> | null;
}

export type NewAlert = Omit<IAlert, 'id'> & { id: null };
