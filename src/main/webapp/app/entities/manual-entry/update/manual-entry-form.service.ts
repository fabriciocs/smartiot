import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IManualEntry, NewManualEntry } from '../manual-entry.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IManualEntry for edit and NewManualEntryFormGroupInput for create.
 */
type ManualEntryFormGroupInput = IManualEntry | PartialWithRequiredKeyOf<NewManualEntry>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IManualEntry | NewManualEntry> = Omit<T, 'entryDate'> & {
  entryDate?: string | null;
};

type ManualEntryFormRawValue = FormValueOf<IManualEntry>;

type NewManualEntryFormRawValue = FormValueOf<NewManualEntry>;

type ManualEntryFormDefaults = Pick<NewManualEntry, 'id' | 'entryDate'>;

type ManualEntryFormGroupContent = {
  id: FormControl<ManualEntryFormRawValue['id'] | NewManualEntry['id']>;
  entryType: FormControl<ManualEntryFormRawValue['entryType']>;
  value: FormControl<ManualEntryFormRawValue['value']>;
  entryDate: FormControl<ManualEntryFormRawValue['entryDate']>;
};

export type ManualEntryFormGroup = FormGroup<ManualEntryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ManualEntryFormService {
  createManualEntryFormGroup(manualEntry: ManualEntryFormGroupInput = { id: null }): ManualEntryFormGroup {
    const manualEntryRawValue = this.convertManualEntryToManualEntryRawValue({
      ...this.getFormDefaults(),
      ...manualEntry,
    });
    return new FormGroup<ManualEntryFormGroupContent>({
      id: new FormControl(
        { value: manualEntryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      entryType: new FormControl(manualEntryRawValue.entryType, {
        validators: [Validators.required],
      }),
      value: new FormControl(manualEntryRawValue.value, {
        validators: [Validators.required],
      }),
      entryDate: new FormControl(manualEntryRawValue.entryDate, {
        validators: [Validators.required],
      }),
    });
  }

  getManualEntry(form: ManualEntryFormGroup): IManualEntry | NewManualEntry {
    return this.convertManualEntryRawValueToManualEntry(form.getRawValue() as ManualEntryFormRawValue | NewManualEntryFormRawValue);
  }

  resetForm(form: ManualEntryFormGroup, manualEntry: ManualEntryFormGroupInput): void {
    const manualEntryRawValue = this.convertManualEntryToManualEntryRawValue({ ...this.getFormDefaults(), ...manualEntry });
    form.reset(
      {
        ...manualEntryRawValue,
        id: { value: manualEntryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ManualEntryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      entryDate: currentTime,
    };
  }

  private convertManualEntryRawValueToManualEntry(
    rawManualEntry: ManualEntryFormRawValue | NewManualEntryFormRawValue,
  ): IManualEntry | NewManualEntry {
    return {
      ...rawManualEntry,
      entryDate: dayjs(rawManualEntry.entryDate, DATE_TIME_FORMAT),
    };
  }

  private convertManualEntryToManualEntryRawValue(
    manualEntry: IManualEntry | (Partial<NewManualEntry> & ManualEntryFormDefaults),
  ): ManualEntryFormRawValue | PartialWithRequiredKeyOf<NewManualEntryFormRawValue> {
    return {
      ...manualEntry,
      entryDate: manualEntry.entryDate ? manualEntry.entryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
