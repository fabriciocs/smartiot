import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 14377,
  nome: 'because hm ick',
  email: '!g@t..]1ZO:',
};

export const sampleWithPartialData: ICliente = {
  id: 23693,
  nome: 'dislodge',
  email: 'LL@7o.q<',
};

export const sampleWithFullData: ICliente = {
  id: 1160,
  nome: 'commonly doff',
  email: '-@Kt(Xf.c',
};

export const sampleWithNewData: NewCliente = {
  nome: 'despite wasting kneel',
  email: "!w'@S#.)oz",
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
