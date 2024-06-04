import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEnrollment } from 'app/entities/enrollment/enrollment.model';
import { EnrollmentService } from 'app/entities/enrollment/service/enrollment.service';
import { IMeasurement } from '../measurement.model';
import { MeasurementService } from '../service/measurement.service';
import { MeasurementFormService, MeasurementFormGroup } from './measurement-form.service';

@Component({
  standalone: true,
  selector: 'jhi-measurement-update',
  templateUrl: './measurement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MeasurementUpdateComponent implements OnInit {
  isSaving = false;
  measurement: IMeasurement | null = null;

  enrollmentsSharedCollection: IEnrollment[] = [];

  protected measurementService = inject(MeasurementService);
  protected measurementFormService = inject(MeasurementFormService);
  protected enrollmentService = inject(EnrollmentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MeasurementFormGroup = this.measurementFormService.createMeasurementFormGroup();

  compareEnrollment = (o1: IEnrollment | null, o2: IEnrollment | null): boolean => this.enrollmentService.compareEnrollment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ measurement }) => {
      this.measurement = measurement;
      if (measurement) {
        this.updateForm(measurement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const measurement = this.measurementFormService.getMeasurement(this.editForm);
    if (measurement.id !== null) {
      this.subscribeToSaveResponse(this.measurementService.update(measurement));
    } else {
      this.subscribeToSaveResponse(this.measurementService.create(measurement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeasurement>>): void {
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

  protected updateForm(measurement: IMeasurement): void {
    this.measurement = measurement;
    this.measurementFormService.resetForm(this.editForm, measurement);

    this.enrollmentsSharedCollection = this.enrollmentService.addEnrollmentToCollectionIfMissing<IEnrollment>(
      this.enrollmentsSharedCollection,
      measurement.enrollment,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.enrollmentService
      .query()
      .pipe(map((res: HttpResponse<IEnrollment[]>) => res.body ?? []))
      .pipe(
        map((enrollments: IEnrollment[]) =>
          this.enrollmentService.addEnrollmentToCollectionIfMissing<IEnrollment>(enrollments, this.measurement?.enrollment),
        ),
      )
      .subscribe((enrollments: IEnrollment[]) => (this.enrollmentsSharedCollection = enrollments));
  }
}
