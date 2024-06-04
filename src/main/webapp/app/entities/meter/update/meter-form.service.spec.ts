import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../meter.test-samples';

import { MeterFormService } from './meter-form.service';

describe('Meter Form Service', () => {
  let service: MeterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MeterFormService);
  });

  describe('Service methods', () => {
    describe('createMeterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMeterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serialNumber: expect.any(Object),
            location: expect.any(Object),
          }),
        );
      });

      it('passing IMeter should create a new form with FormGroup', () => {
        const formGroup = service.createMeterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serialNumber: expect.any(Object),
            location: expect.any(Object),
          }),
        );
      });
    });

    describe('getMeter', () => {
      it('should return NewMeter for default Meter initial value', () => {
        const formGroup = service.createMeterFormGroup(sampleWithNewData);

        const meter = service.getMeter(formGroup) as any;

        expect(meter).toMatchObject(sampleWithNewData);
      });

      it('should return NewMeter for empty Meter initial value', () => {
        const formGroup = service.createMeterFormGroup();

        const meter = service.getMeter(formGroup) as any;

        expect(meter).toMatchObject({});
      });

      it('should return IMeter', () => {
        const formGroup = service.createMeterFormGroup(sampleWithRequiredData);

        const meter = service.getMeter(formGroup) as any;

        expect(meter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMeter should not enable id FormControl', () => {
        const formGroup = service.createMeterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMeter should disable id FormControl', () => {
        const formGroup = service.createMeterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
