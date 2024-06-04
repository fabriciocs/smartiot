import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IManualEntry } from '../manual-entry.model';
import { ManualEntryService } from '../service/manual-entry.service';
import { ManualEntryFormService, ManualEntryFormGroup } from './manual-entry-form.service';

@Component({
  standalone: true,
  selector: 'jhi-manual-entry-update',
  templateUrl: './manual-entry-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ManualEntryUpdateComponent implements OnInit {
  isSaving = false;
  manualEntry: IManualEntry | null = null;

  protected manualEntryService = inject(ManualEntryService);
  protected manualEntryFormService = inject(ManualEntryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ManualEntryFormGroup = this.manualEntryFormService.createManualEntryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manualEntry }) => {
      this.manualEntry = manualEntry;
      if (manualEntry) {
        this.updateForm(manualEntry);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manualEntry = this.manualEntryFormService.getManualEntry(this.editForm);
    if (manualEntry.id !== null) {
      this.subscribeToSaveResponse(this.manualEntryService.update(manualEntry));
    } else {
      this.subscribeToSaveResponse(this.manualEntryService.create(manualEntry));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManualEntry>>): void {
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

  protected updateForm(manualEntry: IManualEntry): void {
    this.manualEntry = manualEntry;
    this.manualEntryFormService.resetForm(this.editForm, manualEntry);
  }
}
