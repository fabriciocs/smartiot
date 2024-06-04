import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRepeater } from '../repeater.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../repeater.test-samples';

import { RepeaterService } from './repeater.service';

const requireRestSample: IRepeater = {
  ...sampleWithRequiredData,
};

describe('Repeater Service', () => {
  let service: RepeaterService;
  let httpMock: HttpTestingController;
  let expectedResult: IRepeater | IRepeater[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RepeaterService);
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

    it('should create a Repeater', () => {
      const repeater = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(repeater).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Repeater', () => {
      const repeater = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(repeater).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Repeater', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Repeater', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Repeater', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRepeaterToCollectionIfMissing', () => {
      it('should add a Repeater to an empty array', () => {
        const repeater: IRepeater = sampleWithRequiredData;
        expectedResult = service.addRepeaterToCollectionIfMissing([], repeater);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(repeater);
      });

      it('should not add a Repeater to an array that contains it', () => {
        const repeater: IRepeater = sampleWithRequiredData;
        const repeaterCollection: IRepeater[] = [
          {
            ...repeater,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRepeaterToCollectionIfMissing(repeaterCollection, repeater);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Repeater to an array that doesn't contain it", () => {
        const repeater: IRepeater = sampleWithRequiredData;
        const repeaterCollection: IRepeater[] = [sampleWithPartialData];
        expectedResult = service.addRepeaterToCollectionIfMissing(repeaterCollection, repeater);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(repeater);
      });

      it('should add only unique Repeater to an array', () => {
        const repeaterArray: IRepeater[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const repeaterCollection: IRepeater[] = [sampleWithRequiredData];
        expectedResult = service.addRepeaterToCollectionIfMissing(repeaterCollection, ...repeaterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const repeater: IRepeater = sampleWithRequiredData;
        const repeater2: IRepeater = sampleWithPartialData;
        expectedResult = service.addRepeaterToCollectionIfMissing([], repeater, repeater2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(repeater);
        expect(expectedResult).toContain(repeater2);
      });

      it('should accept null and undefined values', () => {
        const repeater: IRepeater = sampleWithRequiredData;
        expectedResult = service.addRepeaterToCollectionIfMissing([], null, repeater, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(repeater);
      });

      it('should return initial array if no Repeater is added', () => {
        const repeaterCollection: IRepeater[] = [sampleWithRequiredData];
        expectedResult = service.addRepeaterToCollectionIfMissing(repeaterCollection, undefined, null);
        expect(expectedResult).toEqual(repeaterCollection);
      });
    });

    describe('compareRepeater', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRepeater(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRepeater(entity1, entity2);
        const compareResult2 = service.compareRepeater(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRepeater(entity1, entity2);
        const compareResult2 = service.compareRepeater(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRepeater(entity1, entity2);
        const compareResult2 = service.compareRepeater(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
