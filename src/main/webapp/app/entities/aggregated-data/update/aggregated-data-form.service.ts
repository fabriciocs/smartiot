import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAggregatedData, NewAggregatedData } from '../aggregated-data.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAggregatedData for edit and NewAggregatedDataFormGroupInput for create.
 */
type AggregatedDataFormGroupInput = IAggregatedData | PartialWithRequiredKeyOf<NewAggregatedData>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAggregatedData | NewAggregatedData> = Omit<T, 'aggregationTime'> & {
  aggregationTime?: string | null;
};

type AggregatedDataFormRawValue = FormValueOf<IAggregatedData>;

type NewAggregatedDataFormRawValue = FormValueOf<NewAggregatedData>;

type AggregatedDataFormDefaults = Pick<NewAggregatedData, 'id' | 'aggregationTime'>;

type AggregatedDataFormGroupContent = {
  id: FormControl<AggregatedDataFormRawValue['id'] | NewAggregatedData['id']>;
  dataType: FormControl<AggregatedDataFormRawValue['dataType']>;
  value: FormControl<AggregatedDataFormRawValue['value']>;
  aggregationTime: FormControl<AggregatedDataFormRawValue['aggregationTime']>;
};

export type AggregatedDataFormGroup = FormGroup<AggregatedDataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AggregatedDataFormService {
  createAggregatedDataFormGroup(aggregatedData: AggregatedDataFormGroupInput = { id: null }): AggregatedDataFormGroup {
    const aggregatedDataRawValue = this.convertAggregatedDataToAggregatedDataRawValue({
      ...this.getFormDefaults(),
      ...aggregatedData,
    });
    return new FormGroup<AggregatedDataFormGroupContent>({
      id: new FormControl(
        { value: aggregatedDataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataType: new FormControl(aggregatedDataRawValue.dataType, {
        validators: [Validators.required],
      }),
      value: new FormControl(aggregatedDataRawValue.value, {
        validators: [Validators.required],
      }),
      aggregationTime: new FormControl(aggregatedDataRawValue.aggregationTime, {
        validators: [Validators.required],
      }),
    });
  }

  getAggregatedData(form: AggregatedDataFormGroup): IAggregatedData | NewAggregatedData {
    return this.convertAggregatedDataRawValueToAggregatedData(
      form.getRawValue() as AggregatedDataFormRawValue | NewAggregatedDataFormRawValue,
    );
  }

  resetForm(form: AggregatedDataFormGroup, aggregatedData: AggregatedDataFormGroupInput): void {
    const aggregatedDataRawValue = this.convertAggregatedDataToAggregatedDataRawValue({ ...this.getFormDefaults(), ...aggregatedData });
    form.reset(
      {
        ...aggregatedDataRawValue,
        id: { value: aggregatedDataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AggregatedDataFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      aggregationTime: currentTime,
    };
  }

  private convertAggregatedDataRawValueToAggregatedData(
    rawAggregatedData: AggregatedDataFormRawValue | NewAggregatedDataFormRawValue,
  ): IAggregatedData | NewAggregatedData {
    return {
      ...rawAggregatedData,
      aggregationTime: dayjs(rawAggregatedData.aggregationTime, DATE_TIME_FORMAT),
    };
  }

  private convertAggregatedDataToAggregatedDataRawValue(
    aggregatedData: IAggregatedData | (Partial<NewAggregatedData> & AggregatedDataFormDefaults),
  ): AggregatedDataFormRawValue | PartialWithRequiredKeyOf<NewAggregatedDataFormRawValue> {
    return {
      ...aggregatedData,
      aggregationTime: aggregatedData.aggregationTime ? aggregatedData.aggregationTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
