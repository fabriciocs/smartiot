import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 32110,
  nome: 'healthily opposite prevail',
  email: "<hW'8@lz'\\zK*cu",
};

export const sampleWithPartialData: ICliente = {
  id: 16839,
  nome: 'cheery maltreat',
  email: 'R=w@A#U:|\\yi[T',
};

export const sampleWithFullData: ICliente = {
  id: 28351,
  nome: 'however aw',
  email: "/)Hl%G@t+~'a\\_g{",
};

export const sampleWithNewData: NewCliente = {
  nome: 'irresponsible subtle',
  email: 'Sv@!<\\Oedk',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
