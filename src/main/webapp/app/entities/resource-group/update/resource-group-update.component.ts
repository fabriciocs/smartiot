import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IResourceGroup } from '../resource-group.model';
import { ResourceGroupService } from '../service/resource-group.service';
import { ResourceGroupFormService, ResourceGroupFormGroup } from './resource-group-form.service';

@Component({
  standalone: true,
  selector: 'jhi-resource-group-update',
  templateUrl: './resource-group-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ResourceGroupUpdateComponent implements OnInit {
  isSaving = false;
  resourceGroup: IResourceGroup | null = null;

  protected resourceGroupService = inject(ResourceGroupService);
  protected resourceGroupFormService = inject(ResourceGroupFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ResourceGroupFormGroup = this.resourceGroupFormService.createResourceGroupFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourceGroup }) => {
      this.resourceGroup = resourceGroup;
      if (resourceGroup) {
        this.updateForm(resourceGroup);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resourceGroup = this.resourceGroupFormService.getResourceGroup(this.editForm);
    if (resourceGroup.id !== null) {
      this.subscribeToSaveResponse(this.resourceGroupService.update(resourceGroup));
    } else {
      this.subscribeToSaveResponse(this.resourceGroupService.create(resourceGroup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResourceGroup>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(resourceGroup: IResourceGroup): void {
    this.resourceGroup = resourceGroup;
    this.resourceGroupFormService.resetForm(this.editForm, resourceGroup);
  }
}
