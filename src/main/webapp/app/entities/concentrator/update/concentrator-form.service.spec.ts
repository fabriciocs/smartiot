import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../concentrator.test-samples';

import { ConcentratorFormService } from './concentrator-form.service';

describe('Concentrator Form Service', () => {
  let service: ConcentratorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConcentratorFormService);
  });

  describe('Service methods', () => {
    describe('createConcentratorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConcentratorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serialNumber: expect.any(Object),
            capacity: expect.any(Object),
          }),
        );
      });

      it('passing IConcentrator should create a new form with FormGroup', () => {
        const formGroup = service.createConcentratorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serialNumber: expect.any(Object),
            capacity: expect.any(Object),
          }),
        );
      });
    });

    describe('getConcentrator', () => {
      it('should return NewConcentrator for default Concentrator initial value', () => {
        const formGroup = service.createConcentratorFormGroup(sampleWithNewData);

        const concentrator = service.getConcentrator(formGroup) as any;

        expect(concentrator).toMatchObject(sampleWithNewData);
      });

      it('should return NewConcentrator for empty Concentrator initial value', () => {
        const formGroup = service.createConcentratorFormGroup();

        const concentrator = service.getConcentrator(formGroup) as any;

        expect(concentrator).toMatchObject({});
      });

      it('should return IConcentrator', () => {
        const formGroup = service.createConcentratorFormGroup(sampleWithRequiredData);

        const concentrator = service.getConcentrator(formGroup) as any;

        expect(concentrator).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConcentrator should not enable id FormControl', () => {
        const formGroup = service.createConcentratorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConcentrator should disable id FormControl', () => {
        const formGroup = service.createConcentratorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
