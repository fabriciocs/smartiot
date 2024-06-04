import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITransmitter } from '../transmitter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../transmitter.test-samples';

import { TransmitterService } from './transmitter.service';

const requireRestSample: ITransmitter = {
  ...sampleWithRequiredData,
};

describe('Transmitter Service', () => {
  let service: TransmitterService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransmitter | ITransmitter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransmitterService);
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

    it('should create a Transmitter', () => {
      const transmitter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transmitter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Transmitter', () => {
      const transmitter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transmitter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Transmitter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Transmitter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Transmitter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTransmitterToCollectionIfMissing', () => {
      it('should add a Transmitter to an empty array', () => {
        const transmitter: ITransmitter = sampleWithRequiredData;
        expectedResult = service.addTransmitterToCollectionIfMissing([], transmitter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transmitter);
      });

      it('should not add a Transmitter to an array that contains it', () => {
        const transmitter: ITransmitter = sampleWithRequiredData;
        const transmitterCollection: ITransmitter[] = [
          {
            ...transmitter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransmitterToCollectionIfMissing(transmitterCollection, transmitter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Transmitter to an array that doesn't contain it", () => {
        const transmitter: ITransmitter = sampleWithRequiredData;
        const transmitterCollection: ITransmitter[] = [sampleWithPartialData];
        expectedResult = service.addTransmitterToCollectionIfMissing(transmitterCollection, transmitter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transmitter);
      });

      it('should add only unique Transmitter to an array', () => {
        const transmitterArray: ITransmitter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transmitterCollection: ITransmitter[] = [sampleWithRequiredData];
        expectedResult = service.addTransmitterToCollectionIfMissing(transmitterCollection, ...transmitterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transmitter: ITransmitter = sampleWithRequiredData;
        const transmitter2: ITransmitter = sampleWithPartialData;
        expectedResult = service.addTransmitterToCollectionIfMissing([], transmitter, transmitter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transmitter);
        expect(expectedResult).toContain(transmitter2);
      });

      it('should accept null and undefined values', () => {
        const transmitter: ITransmitter = sampleWithRequiredData;
        expectedResult = service.addTransmitterToCollectionIfMissing([], null, transmitter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transmitter);
      });

      it('should return initial array if no Transmitter is added', () => {
        const transmitterCollection: ITransmitter[] = [sampleWithRequiredData];
        expectedResult = service.addTransmitterToCollectionIfMissing(transmitterCollection, undefined, null);
        expect(expectedResult).toEqual(transmitterCollection);
      });
    });

    describe('compareTransmitter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransmitter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTransmitter(entity1, entity2);
        const compareResult2 = service.compareTransmitter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTransmitter(entity1, entity2);
        const compareResult2 = service.compareTransmitter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTransmitter(entity1, entity2);
        const compareResult2 = service.compareTransmitter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
