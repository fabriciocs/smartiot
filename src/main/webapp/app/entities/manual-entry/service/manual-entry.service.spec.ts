import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IManualEntry } from '../manual-entry.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../manual-entry.test-samples';

import { ManualEntryService, RestManualEntry } from './manual-entry.service';

const requireRestSample: RestManualEntry = {
  ...sampleWithRequiredData,
  entryDate: sampleWithRequiredData.entryDate?.toJSON(),
};

describe('ManualEntry Service', () => {
  let service: ManualEntryService;
  let httpMock: HttpTestingController;
  let expectedResult: IManualEntry | IManualEntry[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ManualEntryService);
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

    it('should create a ManualEntry', () => {
      const manualEntry = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(manualEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ManualEntry', () => {
      const manualEntry = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(manualEntry).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ManualEntry', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ManualEntry', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ManualEntry', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addManualEntryToCollectionIfMissing', () => {
      it('should add a ManualEntry to an empty array', () => {
        const manualEntry: IManualEntry = sampleWithRequiredData;
        expectedResult = service.addManualEntryToCollectionIfMissing([], manualEntry);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manualEntry);
      });

      it('should not add a ManualEntry to an array that contains it', () => {
        const manualEntry: IManualEntry = sampleWithRequiredData;
        const manualEntryCollection: IManualEntry[] = [
          {
            ...manualEntry,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addManualEntryToCollectionIfMissing(manualEntryCollection, manualEntry);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ManualEntry to an array that doesn't contain it", () => {
        const manualEntry: IManualEntry = sampleWithRequiredData;
        const manualEntryCollection: IManualEntry[] = [sampleWithPartialData];
        expectedResult = service.addManualEntryToCollectionIfMissing(manualEntryCollection, manualEntry);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualEntry);
      });

      it('should add only unique ManualEntry to an array', () => {
        const manualEntryArray: IManualEntry[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const manualEntryCollection: IManualEntry[] = [sampleWithRequiredData];
        expectedResult = service.addManualEntryToCollectionIfMissing(manualEntryCollection, ...manualEntryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const manualEntry: IManualEntry = sampleWithRequiredData;
        const manualEntry2: IManualEntry = sampleWithPartialData;
        expectedResult = service.addManualEntryToCollectionIfMissing([], manualEntry, manualEntry2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(manualEntry);
        expect(expectedResult).toContain(manualEntry2);
      });

      it('should accept null and undefined values', () => {
        const manualEntry: IManualEntry = sampleWithRequiredData;
        expectedResult = service.addManualEntryToCollectionIfMissing([], null, manualEntry, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(manualEntry);
      });

      it('should return initial array if no ManualEntry is added', () => {
        const manualEntryCollection: IManualEntry[] = [sampleWithRequiredData];
        expectedResult = service.addManualEntryToCollectionIfMissing(manualEntryCollection, undefined, null);
        expect(expectedResult).toEqual(manualEntryCollection);
      });
    });

    describe('compareManualEntry', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareManualEntry(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareManualEntry(entity1, entity2);
        const compareResult2 = service.compareManualEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareManualEntry(entity1, entity2);
        const compareResult2 = service.compareManualEntry(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareManualEntry(entity1, entity2);
        const compareResult2 = service.compareManualEntry(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
