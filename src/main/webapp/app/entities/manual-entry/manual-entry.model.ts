import dayjs from 'dayjs/esm';

export interface IManualEntry {
  id: number;
  entryType?: string | null;
  value?: string | null;
  entryDate?: dayjs.Dayjs | null;
}

export type NewManualEntry = Omit<IManualEntry, 'id'> & { id: null };
