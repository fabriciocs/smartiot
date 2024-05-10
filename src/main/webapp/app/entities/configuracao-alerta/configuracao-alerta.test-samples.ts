import { IConfiguracaoAlerta, NewConfiguracaoAlerta } from './configuracao-alerta.model';

export const sampleWithRequiredData: IConfiguracaoAlerta = {
  id: 2865,
  email: '#BG@*?\\$3Y?',
};

export const sampleWithPartialData: IConfiguracaoAlerta = {
  id: 3085,
  email: 'KeVp@q{kG;h\\1 ',
};

export const sampleWithFullData: IConfiguracaoAlerta = {
  id: 543,
  limite: 26697.28,
  email: 'kW@S\\&R=pvTj',
};

export const sampleWithNewData: NewConfiguracaoAlerta = {
  email: "#@0',Ce\\e{=>L!F",
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
