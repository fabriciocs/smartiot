import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IResourceGroup, NewResourceGroup } from '../resource-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IResourceGroup for edit and NewResourceGroupFormGroupInput for create.
 */
type ResourceGroupFormGroupInput = IResourceGroup | PartialWithRequiredKeyOf<NewResourceGroup>;

type ResourceGroupFormDefaults = Pick<NewResourceGroup, 'id'>;

type ResourceGroupFormGroupContent = {
  id: FormControl<IResourceGroup['id'] | NewResourceGroup['id']>;
  name: FormControl<IResourceGroup['name']>;
  description: FormControl<IResourceGroup['description']>;
};

export type ResourceGroupFormGroup = FormGroup<ResourceGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ResourceGroupFormService {
  createResourceGroupFormGroup(resourceGroup: ResourceGroupFormGroupInput = { id: null }): ResourceGroupFormGroup {
    const resourceGroupRawValue = {
      ...this.getFormDefaults(),
      ...resourceGroup,
    };
    return new FormGroup<ResourceGroupFormGroupContent>({
      id: new FormControl(
        { value: resourceGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(resourceGroupRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(resourceGroupRawValue.description, {
        validators: [Validators.required],
      }),
    });
  }

  getResourceGroup(form: ResourceGroupFormGroup): IResourceGroup | NewResourceGroup {
    return form.getRawValue() as IResourceGroup | NewResourceGroup;
  }

  resetForm(form: ResourceGroupFormGroup, resourceGroup: ResourceGroupFormGroupInput): void {
    const resourceGroupRawValue = { ...this.getFormDefaults(), ...resourceGroup };
    form.reset(
      {
        ...resourceGroupRawValue,
        id: { value: resourceGroupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ResourceGroupFormDefaults {
    return {
      id: null,
    };
  }
}
