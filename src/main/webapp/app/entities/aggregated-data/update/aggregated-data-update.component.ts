import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAggregatedData } from '../aggregated-data.model';
import { AggregatedDataService } from '../service/aggregated-data.service';
import { AggregatedDataFormService, AggregatedDataFormGroup } from './aggregated-data-form.service';

@Component({
  standalone: true,
  selector: 'jhi-aggregated-data-update',
  templateUrl: './aggregated-data-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AggregatedDataUpdateComponent implements OnInit {
  isSaving = false;
  aggregatedData: IAggregatedData | null = null;

  protected aggregatedDataService = inject(AggregatedDataService);
  protected aggregatedDataFormService = inject(AggregatedDataFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AggregatedDataFormGroup = this.aggregatedDataFormService.createAggregatedDataFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aggregatedData }) => {
      this.aggregatedData = aggregatedData;
      if (aggregatedData) {
        this.updateForm(aggregatedData);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aggregatedData = this.aggregatedDataFormService.getAggregatedData(this.editForm);
    if (aggregatedData.id !== null) {
      this.subscribeToSaveResponse(this.aggregatedDataService.update(aggregatedData));
    } else {
      this.subscribeToSaveResponse(this.aggregatedDataService.create(aggregatedData));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAggregatedData>>): void {
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

  protected updateForm(aggregatedData: IAggregatedData): void {
    this.aggregatedData = aggregatedData;
    this.aggregatedDataFormService.resetForm(this.editForm, aggregatedData);
  }
}
