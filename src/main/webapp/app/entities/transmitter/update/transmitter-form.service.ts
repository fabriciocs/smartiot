import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITransmitter, NewTransmitter } from '../transmitter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransmitter for edit and NewTransmitterFormGroupInput for create.
 */
type TransmitterFormGroupInput = ITransmitter | PartialWithRequiredKeyOf<NewTransmitter>;

type TransmitterFormDefaults = Pick<NewTransmitter, 'id'>;

type TransmitterFormGroupContent = {
  id: FormControl<ITransmitter['id'] | NewTransmitter['id']>;
  serialNumber: FormControl<ITransmitter['serialNumber']>;
  frequency: FormControl<ITransmitter['frequency']>;
};

export type TransmitterFormGroup = FormGroup<TransmitterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransmitterFormService {
  createTransmitterFormGroup(transmitter: TransmitterFormGroupInput = { id: null }): TransmitterFormGroup {
    const transmitterRawValue = {
      ...this.getFormDefaults(),
      ...transmitter,
    };
    return new FormGroup<TransmitterFormGroupContent>({
      id: new FormControl(
        { value: transmitterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serialNumber: new FormControl(transmitterRawValue.serialNumber, {
        validators: [Validators.required],
      }),
      frequency: new FormControl(transmitterRawValue.frequency, {
        validators: [Validators.required],
      }),
    });
  }

  getTransmitter(form: TransmitterFormGroup): ITransmitter | NewTransmitter {
    return form.getRawValue() as ITransmitter | NewTransmitter;
  }

  resetForm(form: TransmitterFormGroup, transmitter: TransmitterFormGroupInput): void {
    const transmitterRawValue = { ...this.getFormDefaults(), ...transmitter };
    form.reset(
      {
        ...transmitterRawValue,
        id: { value: transmitterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransmitterFormDefaults {
    return {
      id: null,
    };
  }
}
