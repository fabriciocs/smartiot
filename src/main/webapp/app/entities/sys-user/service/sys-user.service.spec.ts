import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISysUser } from '../sys-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sys-user.test-samples';

import { SysUserService } from './sys-user.service';

const requireRestSample: ISysUser = {
  ...sampleWithRequiredData,
};

describe('SysUser Service', () => {
  let service: SysUserService;
  let httpMock: HttpTestingController;
  let expectedResult: ISysUser | ISysUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SysUserService);
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

    it('should create a SysUser', () => {
      const sysUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sysUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SysUser', () => {
      const sysUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sysUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SysUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SysUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SysUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSysUserToCollectionIfMissing', () => {
      it('should add a SysUser to an empty array', () => {
        const sysUser: ISysUser = sampleWithRequiredData;
        expectedResult = service.addSysUserToCollectionIfMissing([], sysUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sysUser);
      });

      it('should not add a SysUser to an array that contains it', () => {
        const sysUser: ISysUser = sampleWithRequiredData;
        const sysUserCollection: ISysUser[] = [
          {
            ...sysUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSysUserToCollectionIfMissing(sysUserCollection, sysUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SysUser to an array that doesn't contain it", () => {
        const sysUser: ISysUser = sampleWithRequiredData;
        const sysUserCollection: ISysUser[] = [sampleWithPartialData];
        expectedResult = service.addSysUserToCollectionIfMissing(sysUserCollection, sysUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sysUser);
      });

      it('should add only unique SysUser to an array', () => {
        const sysUserArray: ISysUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sysUserCollection: ISysUser[] = [sampleWithRequiredData];
        expectedResult = service.addSysUserToCollectionIfMissing(sysUserCollection, ...sysUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sysUser: ISysUser = sampleWithRequiredData;
        const sysUser2: ISysUser = sampleWithPartialData;
        expectedResult = service.addSysUserToCollectionIfMissing([], sysUser, sysUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sysUser);
        expect(expectedResult).toContain(sysUser2);
      });

      it('should accept null and undefined values', () => {
        const sysUser: ISysUser = sampleWithRequiredData;
        expectedResult = service.addSysUserToCollectionIfMissing([], null, sysUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sysUser);
      });

      it('should return initial array if no SysUser is added', () => {
        const sysUserCollection: ISysUser[] = [sampleWithRequiredData];
        expectedResult = service.addSysUserToCollectionIfMissing(sysUserCollection, undefined, null);
        expect(expectedResult).toEqual(sysUserCollection);
      });
    });

    describe('compareSysUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSysUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSysUser(entity1, entity2);
        const compareResult2 = service.compareSysUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSysUser(entity1, entity2);
        const compareResult2 = service.compareSysUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSysUser(entity1, entity2);
        const compareResult2 = service.compareSysUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
