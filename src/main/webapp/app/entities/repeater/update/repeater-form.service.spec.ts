import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../repeater.test-samples';

import { RepeaterFormService } from './repeater-form.service';

describe('Repeater Form Service', () => {
  let service: RepeaterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RepeaterFormService);
  });

  describe('Service methods', () => {
    describe('createRepeaterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRepeaterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serialNumber: expect.any(Object),
            range: expect.any(Object),
          }),
        );
      });

      it('passing IRepeater should create a new form with FormGroup', () => {
        const formGroup = service.createRepeaterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serialNumber: expect.any(Object),
            range: expect.any(Object),
          }),
        );
      });
    });

    describe('getRepeater', () => {
      it('should return NewRepeater for default Repeater initial value', () => {
        const formGroup = service.createRepeaterFormGroup(sampleWithNewData);

        const repeater = service.getRepeater(formGroup) as any;

        expect(repeater).toMatchObject(sampleWithNewData);
      });

      it('should return NewRepeater for empty Repeater initial value', () => {
        const formGroup = service.createRepeaterFormGroup();

        const repeater = service.getRepeater(formGroup) as any;

        expect(repeater).toMatchObject({});
      });

      it('should return IRepeater', () => {
        const formGroup = service.createRepeaterFormGroup(sampleWithRequiredData);

        const repeater = service.getRepeater(formGroup) as any;

        expect(repeater).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRepeater should not enable id FormControl', () => {
        const formGroup = service.createRepeaterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRepeater should disable id FormControl', () => {
        const formGroup = service.createRepeaterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
