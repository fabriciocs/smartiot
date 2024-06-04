import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConcentrator } from '../concentrator.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../concentrator.test-samples';

import { ConcentratorService } from './concentrator.service';

const requireRestSample: IConcentrator = {
  ...sampleWithRequiredData,
};

describe('Concentrator Service', () => {
  let service: ConcentratorService;
  let httpMock: HttpTestingController;
  let expectedResult: IConcentrator | IConcentrator[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConcentratorService);
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

    it('should create a Concentrator', () => {
      const concentrator = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(concentrator).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Concentrator', () => {
      const concentrator = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(concentrator).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Concentrator', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Concentrator', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Concentrator', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConcentratorToCollectionIfMissing', () => {
      it('should add a Concentrator to an empty array', () => {
        const concentrator: IConcentrator = sampleWithRequiredData;
        expectedResult = service.addConcentratorToCollectionIfMissing([], concentrator);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(concentrator);
      });

      it('should not add a Concentrator to an array that contains it', () => {
        const concentrator: IConcentrator = sampleWithRequiredData;
        const concentratorCollection: IConcentrator[] = [
          {
            ...concentrator,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConcentratorToCollectionIfMissing(concentratorCollection, concentrator);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Concentrator to an array that doesn't contain it", () => {
        const concentrator: IConcentrator = sampleWithRequiredData;
        const concentratorCollection: IConcentrator[] = [sampleWithPartialData];
        expectedResult = service.addConcentratorToCollectionIfMissing(concentratorCollection, concentrator);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(concentrator);
      });

      it('should add only unique Concentrator to an array', () => {
        const concentratorArray: IConcentrator[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const concentratorCollection: IConcentrator[] = [sampleWithRequiredData];
        expectedResult = service.addConcentratorToCollectionIfMissing(concentratorCollection, ...concentratorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const concentrator: IConcentrator = sampleWithRequiredData;
        const concentrator2: IConcentrator = sampleWithPartialData;
        expectedResult = service.addConcentratorToCollectionIfMissing([], concentrator, concentrator2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(concentrator);
        expect(expectedResult).toContain(concentrator2);
      });

      it('should accept null and undefined values', () => {
        const concentrator: IConcentrator = sampleWithRequiredData;
        expectedResult = service.addConcentratorToCollectionIfMissing([], null, concentrator, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(concentrator);
      });

      it('should return initial array if no Concentrator is added', () => {
        const concentratorCollection: IConcentrator[] = [sampleWithRequiredData];
        expectedResult = service.addConcentratorToCollectionIfMissing(concentratorCollection, undefined, null);
        expect(expectedResult).toEqual(concentratorCollection);
      });
    });

    describe('compareConcentrator', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConcentrator(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConcentrator(entity1, entity2);
        const compareResult2 = service.compareConcentrator(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConcentrator(entity1, entity2);
        const compareResult2 = service.compareConcentrator(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConcentrator(entity1, entity2);
        const compareResult2 = service.compareConcentrator(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
