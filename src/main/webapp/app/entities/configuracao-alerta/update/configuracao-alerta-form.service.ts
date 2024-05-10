import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConfiguracaoAlerta, NewConfiguracaoAlerta } from '../configuracao-alerta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConfiguracaoAlerta for edit and NewConfiguracaoAlertaFormGroupInput for create.
 */
type ConfiguracaoAlertaFormGroupInput = IConfiguracaoAlerta | PartialWithRequiredKeyOf<NewConfiguracaoAlerta>;

type ConfiguracaoAlertaFormDefaults = Pick<NewConfiguracaoAlerta, 'id'>;

type ConfiguracaoAlertaFormGroupContent = {
  id: FormControl<IConfiguracaoAlerta['id'] | NewConfiguracaoAlerta['id']>;
  limite: FormControl<IConfiguracaoAlerta['limite']>;
  email: FormControl<IConfiguracaoAlerta['email']>;
};

export type ConfiguracaoAlertaFormGroup = FormGroup<ConfiguracaoAlertaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConfiguracaoAlertaFormService {
  createConfiguracaoAlertaFormGroup(configuracaoAlerta: ConfiguracaoAlertaFormGroupInput = { id: null }): ConfiguracaoAlertaFormGroup {
    const configuracaoAlertaRawValue = {
      ...this.getFormDefaults(),
      ...configuracaoAlerta,
    };
    return new FormGroup<ConfiguracaoAlertaFormGroupContent>({
      id: new FormControl(
        { value: configuracaoAlertaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      limite: new FormControl(configuracaoAlertaRawValue.limite),
      email: new FormControl(configuracaoAlertaRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^@\\\\s]+@[^@\\\\s]+\\\\.[^@\\\\s]+$')],
      }),
    });
  }

  getConfiguracaoAlerta(form: ConfiguracaoAlertaFormGroup): IConfiguracaoAlerta | NewConfiguracaoAlerta {
    return form.getRawValue() as IConfiguracaoAlerta | NewConfiguracaoAlerta;
  }

  resetForm(form: ConfiguracaoAlertaFormGroup, configuracaoAlerta: ConfiguracaoAlertaFormGroupInput): void {
    const configuracaoAlertaRawValue = { ...this.getFormDefaults(), ...configuracaoAlerta };
    form.reset(
      {
        ...configuracaoAlertaRawValue,
        id: { value: configuracaoAlertaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConfiguracaoAlertaFormDefaults {
    return {
      id: null,
    };
  }
}
