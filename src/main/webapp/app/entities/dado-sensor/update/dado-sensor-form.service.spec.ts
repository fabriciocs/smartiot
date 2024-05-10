import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../dado-sensor.test-samples';

import { DadoSensorFormService } from './dado-sensor-form.service';

describe('DadoSensor Form Service', () => {
  let service: DadoSensorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DadoSensorFormService);
  });

  describe('Service methods', () => {
    describe('createDadoSensorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDadoSensorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dados: expect.any(Object),
            timestamp: expect.any(Object),
          }),
        );
      });

      it('passing IDadoSensor should create a new form with FormGroup', () => {
        const formGroup = service.createDadoSensorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dados: expect.any(Object),
            timestamp: expect.any(Object),
          }),
        );
      });
    });

    describe('getDadoSensor', () => {
      it('should return NewDadoSensor for default DadoSensor initial value', () => {
        const formGroup = service.createDadoSensorFormGroup(sampleWithNewData);

        const dadoSensor = service.getDadoSensor(formGroup) as any;

        expect(dadoSensor).toMatchObject(sampleWithNewData);
      });

      it('should return NewDadoSensor for empty DadoSensor initial value', () => {
        const formGroup = service.createDadoSensorFormGroup();

        const dadoSensor = service.getDadoSensor(formGroup) as any;

        expect(dadoSensor).toMatchObject({});
      });

      it('should return IDadoSensor', () => {
        const formGroup = service.createDadoSensorFormGroup(sampleWithRequiredData);

        const dadoSensor = service.getDadoSensor(formGroup) as any;

        expect(dadoSensor).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDadoSensor should not enable id FormControl', () => {
        const formGroup = service.createDadoSensorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDadoSensor should disable id FormControl', () => {
        const formGroup = service.createDadoSensorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
