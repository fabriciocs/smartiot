import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPricing, NewPricing } from '../pricing.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPricing for edit and NewPricingFormGroupInput for create.
 */
type PricingFormGroupInput = IPricing | PartialWithRequiredKeyOf<NewPricing>;

type PricingFormDefaults = Pick<NewPricing, 'id'>;

type PricingFormGroupContent = {
  id: FormControl<IPricing['id'] | NewPricing['id']>;
  serviceType: FormControl<IPricing['serviceType']>;
  price: FormControl<IPricing['price']>;
};

export type PricingFormGroup = FormGroup<PricingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PricingFormService {
  createPricingFormGroup(pricing: PricingFormGroupInput = { id: null }): PricingFormGroup {
    const pricingRawValue = {
      ...this.getFormDefaults(),
      ...pricing,
    };
    return new FormGroup<PricingFormGroupContent>({
      id: new FormControl(
        { value: pricingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serviceType: new FormControl(pricingRawValue.serviceType, {
        validators: [Validators.required],
      }),
      price: new FormControl(pricingRawValue.price, {
        validators: [Validators.required],
      }),
    });
  }

  getPricing(form: PricingFormGroup): IPricing | NewPricing {
    return form.getRawValue() as IPricing | NewPricing;
  }

  resetForm(form: PricingFormGroup, pricing: PricingFormGroupInput): void {
    const pricingRawValue = { ...this.getFormDefaults(), ...pricing };
    form.reset(
      {
        ...pricingRawValue,
        id: { value: pricingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PricingFormDefaults {
    return {
      id: null,
    };
  }
}
