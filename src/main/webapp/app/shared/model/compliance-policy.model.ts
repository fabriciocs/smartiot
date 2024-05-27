export interface ICompliancePolicy {
  id?: number;
  name?: string;
  description?: string | null;
  rules?: string;
}

export const defaultValue: Readonly<ICompliancePolicy> = {};
