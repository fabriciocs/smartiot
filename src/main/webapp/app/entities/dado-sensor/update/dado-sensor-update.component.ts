import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDadoSensor } from '../dado-sensor.model';
import { DadoSensorService } from '../service/dado-sensor.service';
import { DadoSensorFormService, DadoSensorFormGroup } from './dado-sensor-form.service';

@Component({
  standalone: true,
  selector: 'jhi-dado-sensor-update',
  templateUrl: './dado-sensor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DadoSensorUpdateComponent implements OnInit {
  isSaving = false;
  dadoSensor: IDadoSensor | null = null;

  protected dadoSensorService = inject(DadoSensorService);
  protected dadoSensorFormService = inject(DadoSensorFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DadoSensorFormGroup = this.dadoSensorFormService.createDadoSensorFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dadoSensor }) => {
      this.dadoSensor = dadoSensor;
      if (dadoSensor) {
        this.updateForm(dadoSensor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dadoSensor = this.dadoSensorFormService.getDadoSensor(this.editForm);
    if (dadoSensor.id !== null) {
      this.subscribeToSaveResponse(this.dadoSensorService.update(dadoSensor));
    } else {
      this.subscribeToSaveResponse(this.dadoSensorService.create(dadoSensor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDadoSensor>>): void {
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

  protected updateForm(dadoSensor: IDadoSensor): void {
    this.dadoSensor = dadoSensor;
    this.dadoSensorFormService.resetForm(this.editForm, dadoSensor);
  }
}
