export interface IExternalSystem {
  id?: number;
  name?: string;
  description?: string | null;
  apiEndpoint?: string;
  authDetails?: string;
}

export const defaultValue: Readonly<IExternalSystem> = {};
