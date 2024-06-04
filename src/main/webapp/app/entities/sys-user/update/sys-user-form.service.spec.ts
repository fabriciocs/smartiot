import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sys-user.test-samples';

import { SysUserFormService } from './sys-user-form.service';

describe('SysUser Form Service', () => {
  let service: SysUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SysUserFormService);
  });

  describe('Service methods', () => {
    describe('createSysUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSysUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            email: expect.any(Object),
            role: expect.any(Object),
          }),
        );
      });

      it('passing ISysUser should create a new form with FormGroup', () => {
        const formGroup = service.createSysUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            email: expect.any(Object),
            role: expect.any(Object),
          }),
        );
      });
    });

    describe('getSysUser', () => {
      it('should return NewSysUser for default SysUser initial value', () => {
        const formGroup = service.createSysUserFormGroup(sampleWithNewData);

        const sysUser = service.getSysUser(formGroup) as any;

        expect(sysUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewSysUser for empty SysUser initial value', () => {
        const formGroup = service.createSysUserFormGroup();

        const sysUser = service.getSysUser(formGroup) as any;

        expect(sysUser).toMatchObject({});
      });

      it('should return ISysUser', () => {
        const formGroup = service.createSysUserFormGroup(sampleWithRequiredData);

        const sysUser = service.getSysUser(formGroup) as any;

        expect(sysUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISysUser should not enable id FormControl', () => {
        const formGroup = service.createSysUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSysUser should disable id FormControl', () => {
        const formGroup = service.createSysUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
