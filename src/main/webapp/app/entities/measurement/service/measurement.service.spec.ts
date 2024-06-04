import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMeasurement } from '../measurement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../measurement.test-samples';

import { MeasurementService, RestMeasurement } from './measurement.service';

const requireRestSample: RestMeasurement = {
  ...sampleWithRequiredData,
  measurementTime: sampleWithRequiredData.measurementTime?.toJSON(),
};

describe('Measurement Service', () => {
  let service: MeasurementService;
  let httpMock: HttpTestingController;
  let expectedResult: IMeasurement | IMeasurement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MeasurementService);
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

    it('should create a Measurement', () => {
      const measurement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(measurement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Measurement', () => {
      const measurement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(measurement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Measurement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Measurement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Measurement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMeasurementToCollectionIfMissing', () => {
      it('should add a Measurement to an empty array', () => {
        const measurement: IMeasurement = sampleWithRequiredData;
        expectedResult = service.addMeasurementToCollectionIfMissing([], measurement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(measurement);
      });

      it('should not add a Measurement to an array that contains it', () => {
        const measurement: IMeasurement = sampleWithRequiredData;
        const measurementCollection: IMeasurement[] = [
          {
            ...measurement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMeasurementToCollectionIfMissing(measurementCollection, measurement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Measurement to an array that doesn't contain it", () => {
        const measurement: IMeasurement = sampleWithRequiredData;
        const measurementCollection: IMeasurement[] = [sampleWithPartialData];
        expectedResult = service.addMeasurementToCollectionIfMissing(measurementCollection, measurement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(measurement);
      });

      it('should add only unique Measurement to an array', () => {
        const measurementArray: IMeasurement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const measurementCollection: IMeasurement[] = [sampleWithRequiredData];
        expectedResult = service.addMeasurementToCollectionIfMissing(measurementCollection, ...measurementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const measurement: IMeasurement = sampleWithRequiredData;
        const measurement2: IMeasurement = sampleWithPartialData;
        expectedResult = service.addMeasurementToCollectionIfMissing([], measurement, measurement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(measurement);
        expect(expectedResult).toContain(measurement2);
      });

      it('should accept null and undefined values', () => {
        const measurement: IMeasurement = sampleWithRequiredData;
        expectedResult = service.addMeasurementToCollectionIfMissing([], null, measurement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(measurement);
      });

      it('should return initial array if no Measurement is added', () => {
        const measurementCollection: IMeasurement[] = [sampleWithRequiredData];
        expectedResult = service.addMeasurementToCollectionIfMissing(measurementCollection, undefined, null);
        expect(expectedResult).toEqual(measurementCollection);
      });
    });

    describe('compareMeasurement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMeasurement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMeasurement(entity1, entity2);
        const compareResult2 = service.compareMeasurement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMeasurement(entity1, entity2);
        const compareResult2 = service.compareMeasurement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMeasurement(entity1, entity2);
        const compareResult2 = service.compareMeasurement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
