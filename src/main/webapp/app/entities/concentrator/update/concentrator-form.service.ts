import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConcentrator, NewConcentrator } from '../concentrator.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConcentrator for edit and NewConcentratorFormGroupInput for create.
 */
type ConcentratorFormGroupInput = IConcentrator | PartialWithRequiredKeyOf<NewConcentrator>;

type ConcentratorFormDefaults = Pick<NewConcentrator, 'id'>;

type ConcentratorFormGroupContent = {
  id: FormControl<IConcentrator['id'] | NewConcentrator['id']>;
  serialNumber: FormControl<IConcentrator['serialNumber']>;
  capacity: FormControl<IConcentrator['capacity']>;
};

export type ConcentratorFormGroup = FormGroup<ConcentratorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConcentratorFormService {
  createConcentratorFormGroup(concentrator: ConcentratorFormGroupInput = { id: null }): ConcentratorFormGroup {
    const concentratorRawValue = {
      ...this.getFormDefaults(),
      ...concentrator,
    };
    return new FormGroup<ConcentratorFormGroupContent>({
      id: new FormControl(
        { value: concentratorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serialNumber: new FormControl(concentratorRawValue.serialNumber, {
        validators: [Validators.required],
      }),
      capacity: new FormControl(concentratorRawValue.capacity, {
        validators: [Validators.required],
      }),
    });
  }

  getConcentrator(form: ConcentratorFormGroup): IConcentrator | NewConcentrator {
    return form.getRawValue() as IConcentrator | NewConcentrator;
  }

  resetForm(form: ConcentratorFormGroup, concentrator: ConcentratorFormGroupInput): void {
    const concentratorRawValue = { ...this.getFormDefaults(), ...concentrator };
    form.reset(
      {
        ...concentratorRawValue,
        id: { value: concentratorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConcentratorFormDefaults {
    return {
      id: null,
    };
  }
}
