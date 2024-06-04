export interface IReport {
  id: number;
  reportName?: string | null;
  reportData?: string | null;
}

export type NewReport = Omit<IReport, 'id'> & { id: null };
