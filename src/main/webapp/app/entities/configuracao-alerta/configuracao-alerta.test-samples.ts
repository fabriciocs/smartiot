import { IConfiguracaoAlerta, NewConfiguracaoAlerta } from './configuracao-alerta.model';

export const sampleWithRequiredData: IConfiguracaoAlerta = {
  id: 8902,
  email: '}@C6.r.MO!{',
};

export const sampleWithPartialData: IConfiguracaoAlerta = {
  id: 25931,
  limite: 15809.52,
  email: "|Q$/@'c^.S",
};

export const sampleWithFullData: IConfiguracaoAlerta = {
  id: 8658,
  limite: 17273.21,
  email: '&DE@bO{.O',
};

export const sampleWithNewData: NewConfiguracaoAlerta = {
  email: '4x)@QDhRY.J,',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
