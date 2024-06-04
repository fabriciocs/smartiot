import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../aggregated-data.test-samples';

import { AggregatedDataFormService } from './aggregated-data-form.service';

describe('AggregatedData Form Service', () => {
  let service: AggregatedDataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AggregatedDataFormService);
  });

  describe('Service methods', () => {
    describe('createAggregatedDataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAggregatedDataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataType: expect.any(Object),
            value: expect.any(Object),
            aggregationTime: expect.any(Object),
          }),
        );
      });

      it('passing IAggregatedData should create a new form with FormGroup', () => {
        const formGroup = service.createAggregatedDataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataType: expect.any(Object),
            value: expect.any(Object),
            aggregationTime: expect.any(Object),
          }),
        );
      });
    });

    describe('getAggregatedData', () => {
      it('should return NewAggregatedData for default AggregatedData initial value', () => {
        const formGroup = service.createAggregatedDataFormGroup(sampleWithNewData);

        const aggregatedData = service.getAggregatedData(formGroup) as any;

        expect(aggregatedData).toMatchObject(sampleWithNewData);
      });

      it('should return NewAggregatedData for empty AggregatedData initial value', () => {
        const formGroup = service.createAggregatedDataFormGroup();

        const aggregatedData = service.getAggregatedData(formGroup) as any;

        expect(aggregatedData).toMatchObject({});
      });

      it('should return IAggregatedData', () => {
        const formGroup = service.createAggregatedDataFormGroup(sampleWithRequiredData);

        const aggregatedData = service.getAggregatedData(formGroup) as any;

        expect(aggregatedData).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAggregatedData should not enable id FormControl', () => {
        const formGroup = service.createAggregatedDataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAggregatedData should disable id FormControl', () => {
        const formGroup = service.createAggregatedDataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
