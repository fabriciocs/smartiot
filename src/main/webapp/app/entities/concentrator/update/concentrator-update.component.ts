import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConcentrator } from '../concentrator.model';
import { ConcentratorService } from '../service/concentrator.service';
import { ConcentratorFormService, ConcentratorFormGroup } from './concentrator-form.service';

@Component({
  standalone: true,
  selector: 'jhi-concentrator-update',
  templateUrl: './concentrator-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConcentratorUpdateComponent implements OnInit {
  isSaving = false;
  concentrator: IConcentrator | null = null;

  protected concentratorService = inject(ConcentratorService);
  protected concentratorFormService = inject(ConcentratorFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConcentratorFormGroup = this.concentratorFormService.createConcentratorFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concentrator }) => {
      this.concentrator = concentrator;
      if (concentrator) {
        this.updateForm(concentrator);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const concentrator = this.concentratorFormService.getConcentrator(this.editForm);
    if (concentrator.id !== null) {
      this.subscribeToSaveResponse(this.concentratorService.update(concentrator));
    } else {
      this.subscribeToSaveResponse(this.concentratorService.create(concentrator));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConcentrator>>): void {
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

  protected updateForm(concentrator: IConcentrator): void {
    this.concentrator = concentrator;
    this.concentratorFormService.resetForm(this.editForm, concentrator);
  }
}
