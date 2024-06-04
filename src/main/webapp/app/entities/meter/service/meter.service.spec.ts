import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMeter } from '../meter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../meter.test-samples';

import { MeterService } from './meter.service';

const requireRestSample: IMeter = {
  ...sampleWithRequiredData,
};

describe('Meter Service', () => {
  let service: MeterService;
  let httpMock: HttpTestingController;
  let expectedResult: IMeter | IMeter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MeterService);
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

    it('should create a Meter', () => {
      const meter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(meter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Meter', () => {
      const meter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(meter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Meter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Meter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Meter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMeterToCollectionIfMissing', () => {
      it('should add a Meter to an empty array', () => {
        const meter: IMeter = sampleWithRequiredData;
        expectedResult = service.addMeterToCollectionIfMissing([], meter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meter);
      });

      it('should not add a Meter to an array that contains it', () => {
        const meter: IMeter = sampleWithRequiredData;
        const meterCollection: IMeter[] = [
          {
            ...meter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMeterToCollectionIfMissing(meterCollection, meter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Meter to an array that doesn't contain it", () => {
        const meter: IMeter = sampleWithRequiredData;
        const meterCollection: IMeter[] = [sampleWithPartialData];
        expectedResult = service.addMeterToCollectionIfMissing(meterCollection, meter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meter);
      });

      it('should add only unique Meter to an array', () => {
        const meterArray: IMeter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const meterCollection: IMeter[] = [sampleWithRequiredData];
        expectedResult = service.addMeterToCollectionIfMissing(meterCollection, ...meterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const meter: IMeter = sampleWithRequiredData;
        const meter2: IMeter = sampleWithPartialData;
        expectedResult = service.addMeterToCollectionIfMissing([], meter, meter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meter);
        expect(expectedResult).toContain(meter2);
      });

      it('should accept null and undefined values', () => {
        const meter: IMeter = sampleWithRequiredData;
        expectedResult = service.addMeterToCollectionIfMissing([], null, meter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meter);
      });

      it('should return initial array if no Meter is added', () => {
        const meterCollection: IMeter[] = [sampleWithRequiredData];
        expectedResult = service.addMeterToCollectionIfMissing(meterCollection, undefined, null);
        expect(expectedResult).toEqual(meterCollection);
      });
    });

    describe('compareMeter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMeter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMeter(entity1, entity2);
        const compareResult2 = service.compareMeter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMeter(entity1, entity2);
        const compareResult2 = service.compareMeter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMeter(entity1, entity2);
        const compareResult2 = service.compareMeter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
