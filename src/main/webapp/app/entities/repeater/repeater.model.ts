export interface IRepeater {
  id: number;
  serialNumber?: string | null;
  range?: number | null;
}

export type NewRepeater = Omit<IRepeater, 'id'> & { id: null };
