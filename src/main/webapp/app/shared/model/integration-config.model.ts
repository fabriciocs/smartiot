import dayjs from 'dayjs';
import { IExternalSystem } from 'app/shared/model/external-system.model';

export interface IIntegrationConfig {
  id?: number;
  serviceName?: string;
  configData?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  externalSystem?: IExternalSystem | null;
}

export const defaultValue: Readonly<IIntegrationConfig> = {};
