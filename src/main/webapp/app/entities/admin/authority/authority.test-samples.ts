import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'cfb3494f-2657-4833-85f4-aa5c492a6bf0',
};

export const sampleWithPartialData: IAuthority = {
  name: '7f649a5a-54b7-4f31-98fe-d212d5bf1e27',
};

export const sampleWithFullData: IAuthority = {
  name: '84c2ac4d-94fa-4dbb-90ec-c3893e71591a',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
