import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pricing.test-samples';

import { PricingFormService } from './pricing-form.service';

describe('Pricing Form Service', () => {
  let service: PricingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PricingFormService);
  });

  describe('Service methods', () => {
    describe('createPricingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPricingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceType: expect.any(Object),
            price: expect.any(Object),
          }),
        );
      });

      it('passing IPricing should create a new form with FormGroup', () => {
        const formGroup = service.createPricingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serviceType: expect.any(Object),
            price: expect.any(Object),
          }),
        );
      });
    });

    describe('getPricing', () => {
      it('should return NewPricing for default Pricing initial value', () => {
        const formGroup = service.createPricingFormGroup(sampleWithNewData);

        const pricing = service.getPricing(formGroup) as any;

        expect(pricing).toMatchObject(sampleWithNewData);
      });

      it('should return NewPricing for empty Pricing initial value', () => {
        const formGroup = service.createPricingFormGroup();

        const pricing = service.getPricing(formGroup) as any;

        expect(pricing).toMatchObject({});
      });

      it('should return IPricing', () => {
        const formGroup = service.createPricingFormGroup(sampleWithRequiredData);

        const pricing = service.getPricing(formGroup) as any;

        expect(pricing).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPricing should not enable id FormControl', () => {
        const formGroup = service.createPricingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPricing should disable id FormControl', () => {
        const formGroup = service.createPricingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
