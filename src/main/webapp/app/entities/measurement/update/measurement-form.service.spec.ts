import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../measurement.test-samples';

import { MeasurementFormService } from './measurement-form.service';

describe('Measurement Form Service', () => {
  let service: MeasurementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MeasurementFormService);
  });

  describe('Service methods', () => {
    describe('createMeasurementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMeasurementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            measurementType: expect.any(Object),
            value: expect.any(Object),
            measurementTime: expect.any(Object),
            enrollment: expect.any(Object),
          }),
        );
      });

      it('passing IMeasurement should create a new form with FormGroup', () => {
        const formGroup = service.createMeasurementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            measurementType: expect.any(Object),
            value: expect.any(Object),
            measurementTime: expect.any(Object),
            enrollment: expect.any(Object),
          }),
        );
      });
    });

    describe('getMeasurement', () => {
      it('should return NewMeasurement for default Measurement initial value', () => {
        const formGroup = service.createMeasurementFormGroup(sampleWithNewData);

        const measurement = service.getMeasurement(formGroup) as any;

        expect(measurement).toMatchObject(sampleWithNewData);
      });

      it('should return NewMeasurement for empty Measurement initial value', () => {
        const formGroup = service.createMeasurementFormGroup();

        const measurement = service.getMeasurement(formGroup) as any;

        expect(measurement).toMatchObject({});
      });

      it('should return IMeasurement', () => {
        const formGroup = service.createMeasurementFormGroup(sampleWithRequiredData);

        const measurement = service.getMeasurement(formGroup) as any;

        expect(measurement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMeasurement should not enable id FormControl', () => {
        const formGroup = service.createMeasurementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMeasurement should disable id FormControl', () => {
        const formGroup = service.createMeasurementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
