export interface IResourceGroup {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewResourceGroup = Omit<IResourceGroup, 'id'> & { id: null };
