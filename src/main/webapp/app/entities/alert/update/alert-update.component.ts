import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConsumer } from 'app/entities/consumer/consumer.model';
import { ConsumerService } from 'app/entities/consumer/service/consumer.service';
import { IAlert } from '../alert.model';
import { AlertService } from '../service/alert.service';
import { AlertFormService, AlertFormGroup } from './alert-form.service';

@Component({
  standalone: true,
  selector: 'jhi-alert-update',
  templateUrl: './alert-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlertUpdateComponent implements OnInit {
  isSaving = false;
  alert: IAlert | null = null;

  consumersSharedCollection: IConsumer[] = [];

  protected alertService = inject(AlertService);
  protected alertFormService = inject(AlertFormService);
  protected consumerService = inject(ConsumerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AlertFormGroup = this.alertFormService.createAlertFormGroup();

  compareConsumer = (o1: IConsumer | null, o2: IConsumer | null): boolean => this.consumerService.compareConsumer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alert }) => {
      this.alert = alert;
      if (alert) {
        this.updateForm(alert);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alert = this.alertFormService.getAlert(this.editForm);
    if (alert.id !== null) {
      this.subscribeToSaveResponse(this.alertService.update(alert));
    } else {
      this.subscribeToSaveResponse(this.alertService.create(alert));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlert>>): void {
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

  protected updateForm(alert: IAlert): void {
    this.alert = alert;
    this.alertFormService.resetForm(this.editForm, alert);

    this.consumersSharedCollection = this.consumerService.addConsumerToCollectionIfMissing<IConsumer>(
      this.consumersSharedCollection,
      alert.consumer,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.consumerService
      .query()
      .pipe(map((res: HttpResponse<IConsumer[]>) => res.body ?? []))
      .pipe(
        map((consumers: IConsumer[]) => this.consumerService.addConsumerToCollectionIfMissing<IConsumer>(consumers, this.alert?.consumer)),
      )
      .subscribe((consumers: IConsumer[]) => (this.consumersSharedCollection = consumers));
  }
}
