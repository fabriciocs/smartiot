export interface IConcentrator {
  id: number;
  serialNumber?: string | null;
  capacity?: number | null;
}

export type NewConcentrator = Omit<IConcentrator, 'id'> & { id: null };
