export interface IConsumer {
  id: number;
  name?: string | null;
  street?: string | null;
  neighborhood?: string | null;
  propertyNumber?: number | null;
  phone?: string | null;
  email?: string | null;
}

export type NewConsumer = Omit<IConsumer, 'id'> & { id: null };
