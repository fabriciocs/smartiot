export interface IConfiguracaoAlerta {
  id: number;
  limite?: number | null;
  email?: string | null;
}

export type NewConfiguracaoAlerta = Omit<IConfiguracaoAlerta, 'id'> & { id: null };
