import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISysUser, NewSysUser } from '../sys-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISysUser for edit and NewSysUserFormGroupInput for create.
 */
type SysUserFormGroupInput = ISysUser | PartialWithRequiredKeyOf<NewSysUser>;

type SysUserFormDefaults = Pick<NewSysUser, 'id'>;

type SysUserFormGroupContent = {
  id: FormControl<ISysUser['id'] | NewSysUser['id']>;
  username: FormControl<ISysUser['username']>;
  email: FormControl<ISysUser['email']>;
  role: FormControl<ISysUser['role']>;
};

export type SysUserFormGroup = FormGroup<SysUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SysUserFormService {
  createSysUserFormGroup(sysUser: SysUserFormGroupInput = { id: null }): SysUserFormGroup {
    const sysUserRawValue = {
      ...this.getFormDefaults(),
      ...sysUser,
    };
    return new FormGroup<SysUserFormGroupContent>({
      id: new FormControl(
        { value: sysUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl(sysUserRawValue.username, {
        validators: [Validators.required],
      }),
      email: new FormControl(sysUserRawValue.email, {
        validators: [Validators.required],
      }),
      role: new FormControl(sysUserRawValue.role, {
        validators: [Validators.required],
      }),
    });
  }

  getSysUser(form: SysUserFormGroup): ISysUser | NewSysUser {
    return form.getRawValue() as ISysUser | NewSysUser;
  }

  resetForm(form: SysUserFormGroup, sysUser: SysUserFormGroupInput): void {
    const sysUserRawValue = { ...this.getFormDefaults(), ...sysUser };
    form.reset(
      {
        ...sysUserRawValue,
        id: { value: sysUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SysUserFormDefaults {
    return {
      id: null,
    };
  }
}
