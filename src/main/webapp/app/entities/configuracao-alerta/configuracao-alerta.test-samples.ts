import { IConfiguracaoAlerta, NewConfiguracaoAlerta } from './configuracao-alerta.model';

export const sampleWithRequiredData: IConfiguracaoAlerta = {
  id: 31597,
  email: '9[-@<m#.)O',
};

export const sampleWithPartialData: IConfiguracaoAlerta = {
  id: 12086,
  limite: 13786.39,
  email: '+B@O.ZA',
};

export const sampleWithFullData: IConfiguracaoAlerta = {
  id: 31340,
  limite: 3085.66,
  email: 'LfXq@r{lI<i.0!',
};

export const sampleWithNewData: NewConfiguracaoAlerta = {
  email: 'm@kY.U',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
