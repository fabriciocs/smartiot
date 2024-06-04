export interface IPricing {
  id: number;
  serviceType?: string | null;
  price?: number | null;
}

export type NewPricing = Omit<IPricing, 'id'> & { id: null };
