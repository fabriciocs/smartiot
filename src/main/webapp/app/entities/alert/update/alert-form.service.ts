import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAlert, NewAlert } from '../alert.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlert for edit and NewAlertFormGroupInput for create.
 */
type AlertFormGroupInput = IAlert | PartialWithRequiredKeyOf<NewAlert>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlert | NewAlert> = Omit<T, 'createdDate'> & {
  createdDate?: string | null;
};

type AlertFormRawValue = FormValueOf<IAlert>;

type NewAlertFormRawValue = FormValueOf<NewAlert>;

type AlertFormDefaults = Pick<NewAlert, 'id' | 'createdDate'>;

type AlertFormGroupContent = {
  id: FormControl<AlertFormRawValue['id'] | NewAlert['id']>;
  alertType: FormControl<AlertFormRawValue['alertType']>;
  description: FormControl<AlertFormRawValue['description']>;
  createdDate: FormControl<AlertFormRawValue['createdDate']>;
  consumer: FormControl<AlertFormRawValue['consumer']>;
};

export type AlertFormGroup = FormGroup<AlertFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlertFormService {
  createAlertFormGroup(alert: AlertFormGroupInput = { id: null }): AlertFormGroup {
    const alertRawValue = this.convertAlertToAlertRawValue({
      ...this.getFormDefaults(),
      ...alert,
    });
    return new FormGroup<AlertFormGroupContent>({
      id: new FormControl(
        { value: alertRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      alertType: new FormControl(alertRawValue.alertType, {
        validators: [Validators.required],
      }),
      description: new FormControl(alertRawValue.description, {
        validators: [Validators.required],
      }),
      createdDate: new FormControl(alertRawValue.createdDate, {
        validators: [Validators.required],
      }),
      consumer: new FormControl(alertRawValue.consumer),
    });
  }

  getAlert(form: AlertFormGroup): IAlert | NewAlert {
    return this.convertAlertRawValueToAlert(form.getRawValue() as AlertFormRawValue | NewAlertFormRawValue);
  }

  resetForm(form: AlertFormGroup, alert: AlertFormGroupInput): void {
    const alertRawValue = this.convertAlertToAlertRawValue({ ...this.getFormDefaults(), ...alert });
    form.reset(
      {
        ...alertRawValue,
        id: { value: alertRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlertFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
    };
  }

  private convertAlertRawValueToAlert(rawAlert: AlertFormRawValue | NewAlertFormRawValue): IAlert | NewAlert {
    return {
      ...rawAlert,
      createdDate: dayjs(rawAlert.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertAlertToAlertRawValue(
    alert: IAlert | (Partial<NewAlert> & AlertFormDefaults),
  ): AlertFormRawValue | PartialWithRequiredKeyOf<NewAlertFormRawValue> {
    return {
      ...alert,
      createdDate: alert.createdDate ? alert.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
