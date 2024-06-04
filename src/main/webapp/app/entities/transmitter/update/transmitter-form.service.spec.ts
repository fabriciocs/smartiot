import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../transmitter.test-samples';

import { TransmitterFormService } from './transmitter-form.service';

describe('Transmitter Form Service', () => {
  let service: TransmitterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransmitterFormService);
  });

  describe('Service methods', () => {
    describe('createTransmitterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransmitterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serialNumber: expect.any(Object),
            frequency: expect.any(Object),
          }),
        );
      });

      it('passing ITransmitter should create a new form with FormGroup', () => {
        const formGroup = service.createTransmitterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serialNumber: expect.any(Object),
            frequency: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransmitter', () => {
      it('should return NewTransmitter for default Transmitter initial value', () => {
        const formGroup = service.createTransmitterFormGroup(sampleWithNewData);

        const transmitter = service.getTransmitter(formGroup) as any;

        expect(transmitter).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransmitter for empty Transmitter initial value', () => {
        const formGroup = service.createTransmitterFormGroup();

        const transmitter = service.getTransmitter(formGroup) as any;

        expect(transmitter).toMatchObject({});
      });

      it('should return ITransmitter', () => {
        const formGroup = service.createTransmitterFormGroup(sampleWithRequiredData);

        const transmitter = service.getTransmitter(formGroup) as any;

        expect(transmitter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransmitter should not enable id FormControl', () => {
        const formGroup = service.createTransmitterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransmitter should disable id FormControl', () => {
        const formGroup = service.createTransmitterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
