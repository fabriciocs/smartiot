import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMeasurement, NewMeasurement } from '../measurement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMeasurement for edit and NewMeasurementFormGroupInput for create.
 */
type MeasurementFormGroupInput = IMeasurement | PartialWithRequiredKeyOf<NewMeasurement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMeasurement | NewMeasurement> = Omit<T, 'measurementTime'> & {
  measurementTime?: string | null;
};

type MeasurementFormRawValue = FormValueOf<IMeasurement>;

type NewMeasurementFormRawValue = FormValueOf<NewMeasurement>;

type MeasurementFormDefaults = Pick<NewMeasurement, 'id' | 'measurementTime'>;

type MeasurementFormGroupContent = {
  id: FormControl<MeasurementFormRawValue['id'] | NewMeasurement['id']>;
  measurementType: FormControl<MeasurementFormRawValue['measurementType']>;
  value: FormControl<MeasurementFormRawValue['value']>;
  measurementTime: FormControl<MeasurementFormRawValue['measurementTime']>;
  enrollment: FormControl<MeasurementFormRawValue['enrollment']>;
};

export type MeasurementFormGroup = FormGroup<MeasurementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MeasurementFormService {
  createMeasurementFormGroup(measurement: MeasurementFormGroupInput = { id: null }): MeasurementFormGroup {
    const measurementRawValue = this.convertMeasurementToMeasurementRawValue({
      ...this.getFormDefaults(),
      ...measurement,
    });
    return new FormGroup<MeasurementFormGroupContent>({
      id: new FormControl(
        { value: measurementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      measurementType: new FormControl(measurementRawValue.measurementType, {
        validators: [Validators.required],
      }),
      value: new FormControl(measurementRawValue.value, {
        validators: [Validators.required],
      }),
      measurementTime: new FormControl(measurementRawValue.measurementTime, {
        validators: [Validators.required],
      }),
      enrollment: new FormControl(measurementRawValue.enrollment),
    });
  }

  getMeasurement(form: MeasurementFormGroup): IMeasurement | NewMeasurement {
    return this.convertMeasurementRawValueToMeasurement(form.getRawValue() as MeasurementFormRawValue | NewMeasurementFormRawValue);
  }

  resetForm(form: MeasurementFormGroup, measurement: MeasurementFormGroupInput): void {
    const measurementRawValue = this.convertMeasurementToMeasurementRawValue({ ...this.getFormDefaults(), ...measurement });
    form.reset(
      {
        ...measurementRawValue,
        id: { value: measurementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MeasurementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      measurementTime: currentTime,
    };
  }

  private convertMeasurementRawValueToMeasurement(
    rawMeasurement: MeasurementFormRawValue | NewMeasurementFormRawValue,
  ): IMeasurement | NewMeasurement {
    return {
      ...rawMeasurement,
      measurementTime: dayjs(rawMeasurement.measurementTime, DATE_TIME_FORMAT),
    };
  }

  private convertMeasurementToMeasurementRawValue(
    measurement: IMeasurement | (Partial<NewMeasurement> & MeasurementFormDefaults),
  ): MeasurementFormRawValue | PartialWithRequiredKeyOf<NewMeasurementFormRawValue> {
    return {
      ...measurement,
      measurementTime: measurement.measurementTime ? measurement.measurementTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
