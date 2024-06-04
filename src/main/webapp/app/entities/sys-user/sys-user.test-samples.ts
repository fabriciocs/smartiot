import { ISysUser, NewSysUser } from './sys-user.model';

export const sampleWithRequiredData: ISysUser = {
  id: 7687,
  username: 'duh excepting',
  email: 'Dewitt.Balistreri54@gmail.com',
  role: 'donkey unfasten justly',
};

export const sampleWithPartialData: ISysUser = {
  id: 6648,
  username: 'harmonious than meet',
  email: 'Stephanie.Reilly@yahoo.com',
  role: 'now zowie anenst',
};

export const sampleWithFullData: ISysUser = {
  id: 6854,
  username: 'wobbly while range',
  email: 'Cullen64@gmail.com',
  role: 'actual known',
};

export const sampleWithNewData: NewSysUser = {
  username: 'except phew needily',
  email: 'Aric.Veum@yahoo.com',
  role: 'deglaze',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
