import { IReport, NewReport } from './report.model';

export const sampleWithRequiredData: IReport = {
  id: 23321,
  reportName: 'expand across blueberry',
  reportData: 'as colorfully likewise',
};

export const sampleWithPartialData: IReport = {
  id: 10514,
  reportName: 'boohoo',
  reportData: 'towards aw furthermore',
};

export const sampleWithFullData: IReport = {
  id: 31816,
  reportName: 'any',
  reportData: 'incidentally',
};

export const sampleWithNewData: NewReport = {
  reportName: 'consensus since',
  reportData: 'crisp loot self-reliant',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
