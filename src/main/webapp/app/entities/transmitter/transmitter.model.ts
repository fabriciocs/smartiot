export interface ITransmitter {
  id: number;
  serialNumber?: string | null;
  frequency?: number | null;
}

export type NewTransmitter = Omit<ITransmitter, 'id'> & { id: null };
