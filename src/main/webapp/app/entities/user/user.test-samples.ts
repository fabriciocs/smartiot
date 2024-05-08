import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 13275,
  login: 'g',
};

export const sampleWithPartialData: IUser = {
  id: 30596,
  login: '8n!h6@k',
};

export const sampleWithFullData: IUser = {
  id: 16005,
  login: 'KKvPv',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
