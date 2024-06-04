import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 25066,
  login: 'h@YvPoJd\\[17f\\RPNNwM\\VV',
};

export const sampleWithPartialData: IUser = {
  id: 8953,
  login: 'HwB',
};

export const sampleWithFullData: IUser = {
  id: 27680,
  login: 'G@iFvN-\\rqnSg4P\\63VMXjE\\/Y',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
