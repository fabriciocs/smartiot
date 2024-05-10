import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDadoSensor, NewDadoSensor } from '../dado-sensor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDadoSensor for edit and NewDadoSensorFormGroupInput for create.
 */
type DadoSensorFormGroupInput = IDadoSensor | PartialWithRequiredKeyOf<NewDadoSensor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDadoSensor | NewDadoSensor> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type DadoSensorFormRawValue = FormValueOf<IDadoSensor>;

type NewDadoSensorFormRawValue = FormValueOf<NewDadoSensor>;

type DadoSensorFormDefaults = Pick<NewDadoSensor, 'id' | 'timestamp'>;

type DadoSensorFormGroupContent = {
  id: FormControl<DadoSensorFormRawValue['id'] | NewDadoSensor['id']>;
  dados: FormControl<DadoSensorFormRawValue['dados']>;
  timestamp: FormControl<DadoSensorFormRawValue['timestamp']>;
};

export type DadoSensorFormGroup = FormGroup<DadoSensorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DadoSensorFormService {
  createDadoSensorFormGroup(dadoSensor: DadoSensorFormGroupInput = { id: null }): DadoSensorFormGroup {
    const dadoSensorRawValue = this.convertDadoSensorToDadoSensorRawValue({
      ...this.getFormDefaults(),
      ...dadoSensor,
    });
    return new FormGroup<DadoSensorFormGroupContent>({
      id: new FormControl(
        { value: dadoSensorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dados: new FormControl(dadoSensorRawValue.dados),
      timestamp: new FormControl(dadoSensorRawValue.timestamp),
    });
  }

  getDadoSensor(form: DadoSensorFormGroup): IDadoSensor | NewDadoSensor {
    return this.convertDadoSensorRawValueToDadoSensor(form.getRawValue() as DadoSensorFormRawValue | NewDadoSensorFormRawValue);
  }

  resetForm(form: DadoSensorFormGroup, dadoSensor: DadoSensorFormGroupInput): void {
    const dadoSensorRawValue = this.convertDadoSensorToDadoSensorRawValue({ ...this.getFormDefaults(), ...dadoSensor });
    form.reset(
      {
        ...dadoSensorRawValue,
        id: { value: dadoSensorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DadoSensorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertDadoSensorRawValueToDadoSensor(
    rawDadoSensor: DadoSensorFormRawValue | NewDadoSensorFormRawValue,
  ): IDadoSensor | NewDadoSensor {
    return {
      ...rawDadoSensor,
      timestamp: dayjs(rawDadoSensor.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertDadoSensorToDadoSensorRawValue(
    dadoSensor: IDadoSensor | (Partial<NewDadoSensor> & DadoSensorFormDefaults),
  ): DadoSensorFormRawValue | PartialWithRequiredKeyOf<NewDadoSensorFormRawValue> {
    return {
      ...dadoSensor,
      timestamp: dadoSensor.timestamp ? dadoSensor.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
