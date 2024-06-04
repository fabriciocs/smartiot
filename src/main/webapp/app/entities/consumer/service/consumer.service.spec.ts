import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConsumer } from '../consumer.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../consumer.test-samples';

import { ConsumerService } from './consumer.service';

const requireRestSample: IConsumer = {
  ...sampleWithRequiredData,
};

describe('Consumer Service', () => {
  let service: ConsumerService;
  let httpMock: HttpTestingController;
  let expectedResult: IConsumer | IConsumer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConsumerService);
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

    it('should create a Consumer', () => {
      const consumer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(consumer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Consumer', () => {
      const consumer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(consumer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Consumer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Consumer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Consumer', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConsumerToCollectionIfMissing', () => {
      it('should add a Consumer to an empty array', () => {
        const consumer: IConsumer = sampleWithRequiredData;
        expectedResult = service.addConsumerToCollectionIfMissing([], consumer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(consumer);
      });

      it('should not add a Consumer to an array that contains it', () => {
        const consumer: IConsumer = sampleWithRequiredData;
        const consumerCollection: IConsumer[] = [
          {
            ...consumer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConsumerToCollectionIfMissing(consumerCollection, consumer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Consumer to an array that doesn't contain it", () => {
        const consumer: IConsumer = sampleWithRequiredData;
        const consumerCollection: IConsumer[] = [sampleWithPartialData];
        expectedResult = service.addConsumerToCollectionIfMissing(consumerCollection, consumer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consumer);
      });

      it('should add only unique Consumer to an array', () => {
        const consumerArray: IConsumer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const consumerCollection: IConsumer[] = [sampleWithRequiredData];
        expectedResult = service.addConsumerToCollectionIfMissing(consumerCollection, ...consumerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const consumer: IConsumer = sampleWithRequiredData;
        const consumer2: IConsumer = sampleWithPartialData;
        expectedResult = service.addConsumerToCollectionIfMissing([], consumer, consumer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(consumer);
        expect(expectedResult).toContain(consumer2);
      });

      it('should accept null and undefined values', () => {
        const consumer: IConsumer = sampleWithRequiredData;
        expectedResult = service.addConsumerToCollectionIfMissing([], null, consumer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(consumer);
      });

      it('should return initial array if no Consumer is added', () => {
        const consumerCollection: IConsumer[] = [sampleWithRequiredData];
        expectedResult = service.addConsumerToCollectionIfMissing(consumerCollection, undefined, null);
        expect(expectedResult).toEqual(consumerCollection);
      });
    });

    describe('compareConsumer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConsumer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConsumer(entity1, entity2);
        const compareResult2 = service.compareConsumer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConsumer(entity1, entity2);
        const compareResult2 = service.compareConsumer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConsumer(entity1, entity2);
        const compareResult2 = service.compareConsumer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
