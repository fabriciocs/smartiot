import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 18497,
  nome: 'mmm healthily',
  email: 'gcJ@).IhBz',
};

export const sampleWithPartialData: ICliente = {
  id: 13827,
  nome: 'how',
  email: 'mz(@PM+duQ.nOr',
};

export const sampleWithFullData: ICliente = {
  id: 15491,
  nome: 'alloy shower',
  email: '$V<@zJi]Vr._|]h',
};

export const sampleWithNewData: NewCliente = {
  nome: 'unless',
  email: 'S}0*J@&Ift,.(b_3g{',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
