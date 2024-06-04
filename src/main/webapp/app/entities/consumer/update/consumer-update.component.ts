import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConsumer } from '../consumer.model';
import { ConsumerService } from '../service/consumer.service';
import { ConsumerFormService, ConsumerFormGroup } from './consumer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-consumer-update',
  templateUrl: './consumer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConsumerUpdateComponent implements OnInit {
  isSaving = false;
  consumer: IConsumer | null = null;

  protected consumerService = inject(ConsumerService);
  protected consumerFormService = inject(ConsumerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConsumerFormGroup = this.consumerFormService.createConsumerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consumer }) => {
      this.consumer = consumer;
      if (consumer) {
        this.updateForm(consumer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consumer = this.consumerFormService.getConsumer(this.editForm);
    if (consumer.id !== null) {
      this.subscribeToSaveResponse(this.consumerService.update(consumer));
    } else {
      this.subscribeToSaveResponse(this.consumerService.create(consumer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsumer>>): void {
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

  protected updateForm(consumer: IConsumer): void {
    this.consumer = consumer;
    this.consumerFormService.resetForm(this.editForm, consumer);
  }
}
