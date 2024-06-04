import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMeter, NewMeter } from '../meter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMeter for edit and NewMeterFormGroupInput for create.
 */
type MeterFormGroupInput = IMeter | PartialWithRequiredKeyOf<NewMeter>;

type MeterFormDefaults = Pick<NewMeter, 'id'>;

type MeterFormGroupContent = {
  id: FormControl<IMeter['id'] | NewMeter['id']>;
  serialNumber: FormControl<IMeter['serialNumber']>;
  location: FormControl<IMeter['location']>;
};

export type MeterFormGroup = FormGroup<MeterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MeterFormService {
  createMeterFormGroup(meter: MeterFormGroupInput = { id: null }): MeterFormGroup {
    const meterRawValue = {
      ...this.getFormDefaults(),
      ...meter,
    };
    return new FormGroup<MeterFormGroupContent>({
      id: new FormControl(
        { value: meterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serialNumber: new FormControl(meterRawValue.serialNumber, {
        validators: [Validators.required],
      }),
      location: new FormControl(meterRawValue.location, {
        validators: [Validators.required],
      }),
    });
  }

  getMeter(form: MeterFormGroup): IMeter | NewMeter {
    return form.getRawValue() as IMeter | NewMeter;
  }

  resetForm(form: MeterFormGroup, meter: MeterFormGroupInput): void {
    const meterRawValue = { ...this.getFormDefaults(), ...meter };
    form.reset(
      {
        ...meterRawValue,
        id: { value: meterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MeterFormDefaults {
    return {
      id: null,
    };
  }
}
