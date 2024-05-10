import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConfiguracaoAlerta } from '../configuracao-alerta.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../configuracao-alerta.test-samples';

import { ConfiguracaoAlertaService } from './configuracao-alerta.service';

const requireRestSample: IConfiguracaoAlerta = {
  ...sampleWithRequiredData,
};

describe('ConfiguracaoAlerta Service', () => {
  let service: ConfiguracaoAlertaService;
  let httpMock: HttpTestingController;
  let expectedResult: IConfiguracaoAlerta | IConfiguracaoAlerta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConfiguracaoAlertaService);
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

    it('should create a ConfiguracaoAlerta', () => {
      const configuracaoAlerta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(configuracaoAlerta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConfiguracaoAlerta', () => {
      const configuracaoAlerta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(configuracaoAlerta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConfiguracaoAlerta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConfiguracaoAlerta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ConfiguracaoAlerta', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConfiguracaoAlertaToCollectionIfMissing', () => {
      it('should add a ConfiguracaoAlerta to an empty array', () => {
        const configuracaoAlerta: IConfiguracaoAlerta = sampleWithRequiredData;
        expectedResult = service.addConfiguracaoAlertaToCollectionIfMissing([], configuracaoAlerta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configuracaoAlerta);
      });

      it('should not add a ConfiguracaoAlerta to an array that contains it', () => {
        const configuracaoAlerta: IConfiguracaoAlerta = sampleWithRequiredData;
        const configuracaoAlertaCollection: IConfiguracaoAlerta[] = [
          {
            ...configuracaoAlerta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConfiguracaoAlertaToCollectionIfMissing(configuracaoAlertaCollection, configuracaoAlerta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConfiguracaoAlerta to an array that doesn't contain it", () => {
        const configuracaoAlerta: IConfiguracaoAlerta = sampleWithRequiredData;
        const configuracaoAlertaCollection: IConfiguracaoAlerta[] = [sampleWithPartialData];
        expectedResult = service.addConfiguracaoAlertaToCollectionIfMissing(configuracaoAlertaCollection, configuracaoAlerta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configuracaoAlerta);
      });

      it('should add only unique ConfiguracaoAlerta to an array', () => {
        const configuracaoAlertaArray: IConfiguracaoAlerta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const configuracaoAlertaCollection: IConfiguracaoAlerta[] = [sampleWithRequiredData];
        expectedResult = service.addConfiguracaoAlertaToCollectionIfMissing(configuracaoAlertaCollection, ...configuracaoAlertaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const configuracaoAlerta: IConfiguracaoAlerta = sampleWithRequiredData;
        const configuracaoAlerta2: IConfiguracaoAlerta = sampleWithPartialData;
        expectedResult = service.addConfiguracaoAlertaToCollectionIfMissing([], configuracaoAlerta, configuracaoAlerta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configuracaoAlerta);
        expect(expectedResult).toContain(configuracaoAlerta2);
      });

      it('should accept null and undefined values', () => {
        const configuracaoAlerta: IConfiguracaoAlerta = sampleWithRequiredData;
        expectedResult = service.addConfiguracaoAlertaToCollectionIfMissing([], null, configuracaoAlerta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configuracaoAlerta);
      });

      it('should return initial array if no ConfiguracaoAlerta is added', () => {
        const configuracaoAlertaCollection: IConfiguracaoAlerta[] = [sampleWithRequiredData];
        expectedResult = service.addConfiguracaoAlertaToCollectionIfMissing(configuracaoAlertaCollection, undefined, null);
        expect(expectedResult).toEqual(configuracaoAlertaCollection);
      });
    });

    describe('compareConfiguracaoAlerta', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConfiguracaoAlerta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareConfiguracaoAlerta(entity1, entity2);
        const compareResult2 = service.compareConfiguracaoAlerta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareConfiguracaoAlerta(entity1, entity2);
        const compareResult2 = service.compareConfiguracaoAlerta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareConfiguracaoAlerta(entity1, entity2);
        const compareResult2 = service.compareConfiguracaoAlerta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
