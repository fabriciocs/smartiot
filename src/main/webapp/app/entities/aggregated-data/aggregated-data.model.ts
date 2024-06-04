import dayjs from 'dayjs/esm';

export interface IAggregatedData {
  id: number;
  dataType?: string | null;
  value?: string | null;
  aggregationTime?: dayjs.Dayjs | null;
}

export type NewAggregatedData = Omit<IAggregatedData, 'id'> & { id: null };
