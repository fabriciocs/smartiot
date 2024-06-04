export interface IMeter {
  id: number;
  serialNumber?: string | null;
  location?: string | null;
}

export type NewMeter = Omit<IMeter, 'id'> & { id: null };
