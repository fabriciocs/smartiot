import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRepeater, NewRepeater } from '../repeater.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRepeater for edit and NewRepeaterFormGroupInput for create.
 */
type RepeaterFormGroupInput = IRepeater | PartialWithRequiredKeyOf<NewRepeater>;

type RepeaterFormDefaults = Pick<NewRepeater, 'id'>;

type RepeaterFormGroupContent = {
  id: FormControl<IRepeater['id'] | NewRepeater['id']>;
  serialNumber: FormControl<IRepeater['serialNumber']>;
  range: FormControl<IRepeater['range']>;
};

export type RepeaterFormGroup = FormGroup<RepeaterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RepeaterFormService {
  createRepeaterFormGroup(repeater: RepeaterFormGroupInput = { id: null }): RepeaterFormGroup {
    const repeaterRawValue = {
      ...this.getFormDefaults(),
      ...repeater,
    };
    return new FormGroup<RepeaterFormGroupContent>({
      id: new FormControl(
        { value: repeaterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serialNumber: new FormControl(repeaterRawValue.serialNumber, {
        validators: [Validators.required],
      }),
      range: new FormControl(repeaterRawValue.range, {
        validators: [Validators.required],
      }),
    });
  }

  getRepeater(form: RepeaterFormGroup): IRepeater | NewRepeater {
    return form.getRawValue() as IRepeater | NewRepeater;
  }

  resetForm(form: RepeaterFormGroup, repeater: RepeaterFormGroupInput): void {
    const repeaterRawValue = { ...this.getFormDefaults(), ...repeater };
    form.reset(
      {
        ...repeaterRawValue,
        id: { value: repeaterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RepeaterFormDefaults {
    return {
      id: null,
    };
  }
}
