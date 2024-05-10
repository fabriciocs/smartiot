import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDadoSensor } from '../dado-sensor.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../dado-sensor.test-samples';

import { DadoSensorService, RestDadoSensor } from './dado-sensor.service';

const requireRestSample: RestDadoSensor = {
  ...sampleWithRequiredData,
  timestamp: sampleWithRequiredData.timestamp?.toJSON(),
};

describe('DadoSensor Service', () => {
  let service: DadoSensorService;
  let httpMock: HttpTestingController;
  let expectedResult: IDadoSensor | IDadoSensor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DadoSensorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a DadoSensor', () => {
      const dadoSensor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dadoSensor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DadoSensor', () => {
      const dadoSensor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dadoSensor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DadoSensor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DadoSensor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DadoSensor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDadoSensorToCollectionIfMissing', () => {
      it('should add a DadoSensor to an empty array', () => {
        const dadoSensor: IDadoSensor = sampleWithRequiredData;
        expectedResult = service.addDadoSensorToCollectionIfMissing([], dadoSensor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dadoSensor);
      });

      it('should not add a DadoSensor to an array that contains it', () => {
        const dadoSensor: IDadoSensor = sampleWithRequiredData;
        const dadoSensorCollection: IDadoSensor[] = [
          {
            ...dadoSensor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDadoSensorToCollectionIfMissing(dadoSensorCollection, dadoSensor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DadoSensor to an array that doesn't contain it", () => {
        const dadoSensor: IDadoSensor = sampleWithRequiredData;
        const dadoSensorCollection: IDadoSensor[] = [sampleWithPartialData];
        expectedResult = service.addDadoSensorToCollectionIfMissing(dadoSensorCollection, dadoSensor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dadoSensor);
      });

      it('should add only unique DadoSensor to an array', () => {
        const dadoSensorArray: IDadoSensor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dadoSensorCollection: IDadoSensor[] = [sampleWithRequiredData];
        expectedResult = service.addDadoSensorToCollectionIfMissing(dadoSensorCollection, ...dadoSensorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dadoSensor: IDadoSensor = sampleWithRequiredData;
        const dadoSensor2: IDadoSensor = sampleWithPartialData;
        expectedResult = service.addDadoSensorToCollectionIfMissing([], dadoSensor, dadoSensor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dadoSensor);
        expect(expectedResult).toContain(dadoSensor2);
      });

      it('should accept null and undefined values', () => {
        const dadoSensor: IDadoSensor = sampleWithRequiredData;
        expectedResult = service.addDadoSensorToCollectionIfMissing([], null, dadoSensor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dadoSensor);
      });

      it('should return initial array if no DadoSensor is added', () => {
        const dadoSensorCollection: IDadoSensor[] = [sampleWithRequiredData];
        expectedResult = service.addDadoSensorToCollectionIfMissing(dadoSensorCollection, undefined, null);
        expect(expectedResult).toEqual(dadoSensorCollection);
      });
    });

    describe('compareDadoSensor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDadoSensor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDadoSensor(entity1, entity2);
        const compareResult2 = service.compareDadoSensor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDadoSensor(entity1, entity2);
        const compareResult2 = service.compareDadoSensor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDadoSensor(entity1, entity2);
        const compareResult2 = service.compareDadoSensor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
