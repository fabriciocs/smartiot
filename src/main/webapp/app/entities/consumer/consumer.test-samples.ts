import { IConsumer, NewConsumer } from './consumer.model';

export const sampleWithRequiredData: IConsumer = {
  id: 7421,
  name: 'mmm importance madly',
  street: 'Claudine Heights',
  neighborhood: 'towards',
  propertyNumber: 32504,
  phone: '1-377-448-0819 x155',
  email: 'Gudrun71@gmail.com',
};

export const sampleWithPartialData: IConsumer = {
  id: 29431,
  name: 'enthusiastically sacrifice hourly',
  street: 'Ferry Court',
  neighborhood: 'positively',
  propertyNumber: 8320,
  phone: '885.851.6420 x724',
  email: 'Rowland_Bauch@hotmail.com',
};

export const sampleWithFullData: IConsumer = {
  id: 9872,
  name: 'upon counter-force',
  street: 'Waters Camp',
  neighborhood: 'hunger',
  propertyNumber: 13055,
  phone: '(413) 442-2716',
  email: 'Mariana65@hotmail.com',
};

export const sampleWithNewData: NewConsumer = {
  name: 'unique excluding',
  street: 'Bauch Plains',
  neighborhood: 'push boo',
  propertyNumber: 19769,
  phone: '1-562-299-0025 x797',
  email: 'Robbie65@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
