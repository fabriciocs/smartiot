export interface ICostCenter {
  id: number;
  name?: string | null;
  budget?: number | null;
}

export type NewCostCenter = Omit<ICostCenter, 'id'> & { id: null };
