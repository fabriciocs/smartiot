import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRepeater } from '../repeater.model';
import { RepeaterService } from '../service/repeater.service';
import { RepeaterFormService, RepeaterFormGroup } from './repeater-form.service';

@Component({
  standalone: true,
  selector: 'jhi-repeater-update',
  templateUrl: './repeater-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RepeaterUpdateComponent implements OnInit {
  isSaving = false;
  repeater: IRepeater | null = null;

  protected repeaterService = inject(RepeaterService);
  protected repeaterFormService = inject(RepeaterFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RepeaterFormGroup = this.repeaterFormService.createRepeaterFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ repeater }) => {
      this.repeater = repeater;
      if (repeater) {
        this.updateForm(repeater);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const repeater = this.repeaterFormService.getRepeater(this.editForm);
    if (repeater.id !== null) {
      this.subscribeToSaveResponse(this.repeaterService.update(repeater));
    } else {
      this.subscribeToSaveResponse(this.repeaterService.create(repeater));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRepeater>>): void {
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

  protected updateForm(repeater: IRepeater): void {
    this.repeater = repeater;
    this.repeaterFormService.resetForm(this.editForm, repeater);
  }
}
