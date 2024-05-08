import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'e5ffc40b-70f1-432a-acdd-d0da4aec3882',
};

export const sampleWithPartialData: IAuthority = {
  name: '38958428-6896-4e5d-84b8-5e37c5f7bd1f',
};

export const sampleWithFullData: IAuthority = {
  name: '0ce42fc4-3fcd-4504-8405-195f1b77c742',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
