import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMeter } from '../meter.model';
import { MeterService } from '../service/meter.service';
import { MeterFormService, MeterFormGroup } from './meter-form.service';

@Component({
  standalone: true,
  selector: 'jhi-meter-update',
  templateUrl: './meter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MeterUpdateComponent implements OnInit {
  isSaving = false;
  meter: IMeter | null = null;

  protected meterService = inject(MeterService);
  protected meterFormService = inject(MeterFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MeterFormGroup = this.meterFormService.createMeterFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meter }) => {
      this.meter = meter;
      if (meter) {
        this.updateForm(meter);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const meter = this.meterFormService.getMeter(this.editForm);
    if (meter.id !== null) {
      this.subscribeToSaveResponse(this.meterService.update(meter));
    } else {
      this.subscribeToSaveResponse(this.meterService.create(meter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeter>>): void {
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

  protected updateForm(meter: IMeter): void {
    this.meter = meter;
    this.meterFormService.resetForm(this.editForm, meter);
  }
}
