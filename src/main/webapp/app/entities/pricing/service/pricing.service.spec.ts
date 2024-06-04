import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPricing } from '../pricing.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pricing.test-samples';

import { PricingService } from './pricing.service';

const requireRestSample: IPricing = {
  ...sampleWithRequiredData,
};

describe('Pricing Service', () => {
  let service: PricingService;
  let httpMock: HttpTestingController;
  let expectedResult: IPricing | IPricing[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PricingService);
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

    it('should create a Pricing', () => {
      const pricing = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pricing).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pricing', () => {
      const pricing = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pricing).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pricing', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pricing', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pricing', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPricingToCollectionIfMissing', () => {
      it('should add a Pricing to an empty array', () => {
        const pricing: IPricing = sampleWithRequiredData;
        expectedResult = service.addPricingToCollectionIfMissing([], pricing);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pricing);
      });

      it('should not add a Pricing to an array that contains it', () => {
        const pricing: IPricing = sampleWithRequiredData;
        const pricingCollection: IPricing[] = [
          {
            ...pricing,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPricingToCollectionIfMissing(pricingCollection, pricing);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pricing to an array that doesn't contain it", () => {
        const pricing: IPricing = sampleWithRequiredData;
        const pricingCollection: IPricing[] = [sampleWithPartialData];
        expectedResult = service.addPricingToCollectionIfMissing(pricingCollection, pricing);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pricing);
      });

      it('should add only unique Pricing to an array', () => {
        const pricingArray: IPricing[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pricingCollection: IPricing[] = [sampleWithRequiredData];
        expectedResult = service.addPricingToCollectionIfMissing(pricingCollection, ...pricingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pricing: IPricing = sampleWithRequiredData;
        const pricing2: IPricing = sampleWithPartialData;
        expectedResult = service.addPricingToCollectionIfMissing([], pricing, pricing2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pricing);
        expect(expectedResult).toContain(pricing2);
      });

      it('should accept null and undefined values', () => {
        const pricing: IPricing = sampleWithRequiredData;
        expectedResult = service.addPricingToCollectionIfMissing([], null, pricing, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pricing);
      });

      it('should return initial array if no Pricing is added', () => {
        const pricingCollection: IPricing[] = [sampleWithRequiredData];
        expectedResult = service.addPricingToCollectionIfMissing(pricingCollection, undefined, null);
        expect(expectedResult).toEqual(pricingCollection);
      });
    });

    describe('comparePricing', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePricing(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePricing(entity1, entity2);
        const compareResult2 = service.comparePricing(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePricing(entity1, entity2);
        const compareResult2 = service.comparePricing(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePricing(entity1, entity2);
        const compareResult2 = service.comparePricing(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
