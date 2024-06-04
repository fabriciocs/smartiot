import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAggregatedData } from '../aggregated-data.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../aggregated-data.test-samples';

import { AggregatedDataService, RestAggregatedData } from './aggregated-data.service';

const requireRestSample: RestAggregatedData = {
  ...sampleWithRequiredData,
  aggregationTime: sampleWithRequiredData.aggregationTime?.toJSON(),
};

describe('AggregatedData Service', () => {
  let service: AggregatedDataService;
  let httpMock: HttpTestingController;
  let expectedResult: IAggregatedData | IAggregatedData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AggregatedDataService);
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

    it('should create a AggregatedData', () => {
      const aggregatedData = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aggregatedData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AggregatedData', () => {
      const aggregatedData = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aggregatedData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AggregatedData', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AggregatedData', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AggregatedData', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAggregatedDataToCollectionIfMissing', () => {
      it('should add a AggregatedData to an empty array', () => {
        const aggregatedData: IAggregatedData = sampleWithRequiredData;
        expectedResult = service.addAggregatedDataToCollectionIfMissing([], aggregatedData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aggregatedData);
      });

      it('should not add a AggregatedData to an array that contains it', () => {
        const aggregatedData: IAggregatedData = sampleWithRequiredData;
        const aggregatedDataCollection: IAggregatedData[] = [
          {
            ...aggregatedData,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAggregatedDataToCollectionIfMissing(aggregatedDataCollection, aggregatedData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AggregatedData to an array that doesn't contain it", () => {
        const aggregatedData: IAggregatedData = sampleWithRequiredData;
        const aggregatedDataCollection: IAggregatedData[] = [sampleWithPartialData];
        expectedResult = service.addAggregatedDataToCollectionIfMissing(aggregatedDataCollection, aggregatedData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aggregatedData);
      });

      it('should add only unique AggregatedData to an array', () => {
        const aggregatedDataArray: IAggregatedData[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aggregatedDataCollection: IAggregatedData[] = [sampleWithRequiredData];
        expectedResult = service.addAggregatedDataToCollectionIfMissing(aggregatedDataCollection, ...aggregatedDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aggregatedData: IAggregatedData = sampleWithRequiredData;
        const aggregatedData2: IAggregatedData = sampleWithPartialData;
        expectedResult = service.addAggregatedDataToCollectionIfMissing([], aggregatedData, aggregatedData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aggregatedData);
        expect(expectedResult).toContain(aggregatedData2);
      });

      it('should accept null and undefined values', () => {
        const aggregatedData: IAggregatedData = sampleWithRequiredData;
        expectedResult = service.addAggregatedDataToCollectionIfMissing([], null, aggregatedData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aggregatedData);
      });

      it('should return initial array if no AggregatedData is added', () => {
        const aggregatedDataCollection: IAggregatedData[] = [sampleWithRequiredData];
        expectedResult = service.addAggregatedDataToCollectionIfMissing(aggregatedDataCollection, undefined, null);
        expect(expectedResult).toEqual(aggregatedDataCollection);
      });
    });

    describe('compareAggregatedData', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAggregatedData(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAggregatedData(entity1, entity2);
        const compareResult2 = service.compareAggregatedData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAggregatedData(entity1, entity2);
        const compareResult2 = service.compareAggregatedData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAggregatedData(entity1, entity2);
        const compareResult2 = service.compareAggregatedData(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
