import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITransmitter } from '../transmitter.model';
import { TransmitterService } from '../service/transmitter.service';
import { TransmitterFormService, TransmitterFormGroup } from './transmitter-form.service';

@Component({
  standalone: true,
  selector: 'jhi-transmitter-update',
  templateUrl: './transmitter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransmitterUpdateComponent implements OnInit {
  isSaving = false;
  transmitter: ITransmitter | null = null;

  protected transmitterService = inject(TransmitterService);
  protected transmitterFormService = inject(TransmitterFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransmitterFormGroup = this.transmitterFormService.createTransmitterFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transmitter }) => {
      this.transmitter = transmitter;
      if (transmitter) {
        this.updateForm(transmitter);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transmitter = this.transmitterFormService.getTransmitter(this.editForm);
    if (transmitter.id !== null) {
      this.subscribeToSaveResponse(this.transmitterService.update(transmitter));
    } else {
      this.subscribeToSaveResponse(this.transmitterService.create(transmitter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransmitter>>): void {
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

  protected updateForm(transmitter: ITransmitter): void {
    this.transmitter = transmitter;
    this.transmitterFormService.resetForm(this.editForm, transmitter);
  }
}
