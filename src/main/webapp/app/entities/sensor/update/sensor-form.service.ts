import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISensor, NewSensor } from '../sensor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISensor for edit and NewSensorFormGroupInput for create.
 */
type SensorFormGroupInput = ISensor | PartialWithRequiredKeyOf<NewSensor>;

type SensorFormDefaults = Pick<NewSensor, 'id'>;

type SensorFormGroupContent = {
  id: FormControl<ISensor['id'] | NewSensor['id']>;
  nome: FormControl<ISensor['nome']>;
  tipo: FormControl<ISensor['tipo']>;
  configuracao: FormControl<ISensor['configuracao']>;
  configuracaoAlertas: FormControl<ISensor['configuracaoAlertas']>;
  dadoSensores: FormControl<ISensor['dadoSensores']>;
};

export type SensorFormGroup = FormGroup<SensorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SensorFormService {
  createSensorFormGroup(sensor: SensorFormGroupInput = { id: null }): SensorFormGroup {
    const sensorRawValue = {
      ...this.getFormDefaults(),
      ...sensor,
    };
    return new FormGroup<SensorFormGroupContent>({
      id: new FormControl(
        { value: sensorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(sensorRawValue.nome, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      tipo: new FormControl(sensorRawValue.tipo, {
        validators: [Validators.required],
      }),
      configuracao: new FormControl(sensorRawValue.configuracao),
      configuracaoAlertas: new FormControl(sensorRawValue.configuracaoAlertas),
      dadoSensores: new FormControl(sensorRawValue.dadoSensores),
    });
  }

  getSensor(form: SensorFormGroup): ISensor | NewSensor {
    return form.getRawValue() as ISensor | NewSensor;
  }

  resetForm(form: SensorFormGroup, sensor: SensorFormGroupInput): void {
    const sensorRawValue = { ...this.getFormDefaults(), ...sensor };
    form.reset(
      {
        ...sensorRawValue,
        id: { value: sensorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SensorFormDefaults {
    return {
      id: null,
    };
  }
}
