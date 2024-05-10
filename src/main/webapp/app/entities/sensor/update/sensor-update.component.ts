import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConfiguracaoAlerta } from 'app/entities/configuracao-alerta/configuracao-alerta.model';
import { ConfiguracaoAlertaService } from 'app/entities/configuracao-alerta/service/configuracao-alerta.service';
import { IDadoSensor } from 'app/entities/dado-sensor/dado-sensor.model';
import { DadoSensorService } from 'app/entities/dado-sensor/service/dado-sensor.service';
import { TipoSensor } from 'app/entities/enumerations/tipo-sensor.model';
import { SensorService } from '../service/sensor.service';
import { ISensor } from '../sensor.model';
import { SensorFormService, SensorFormGroup } from './sensor-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sensor-update',
  templateUrl: './sensor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SensorUpdateComponent implements OnInit {
  isSaving = false;
  sensor: ISensor | null = null;
  tipoSensorValues = Object.keys(TipoSensor);

  configuracaoAlertasSharedCollection: IConfiguracaoAlerta[] = [];
  dadoSensorsSharedCollection: IDadoSensor[] = [];

  protected sensorService = inject(SensorService);
  protected sensorFormService = inject(SensorFormService);
  protected configuracaoAlertaService = inject(ConfiguracaoAlertaService);
  protected dadoSensorService = inject(DadoSensorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SensorFormGroup = this.sensorFormService.createSensorFormGroup();

  compareConfiguracaoAlerta = (o1: IConfiguracaoAlerta | null, o2: IConfiguracaoAlerta | null): boolean =>
    this.configuracaoAlertaService.compareConfiguracaoAlerta(o1, o2);

  compareDadoSensor = (o1: IDadoSensor | null, o2: IDadoSensor | null): boolean => this.dadoSensorService.compareDadoSensor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sensor }) => {
      this.sensor = sensor;
      if (sensor) {
        this.updateForm(sensor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sensor = this.sensorFormService.getSensor(this.editForm);
    if (sensor.id !== null) {
      this.subscribeToSaveResponse(this.sensorService.update(sensor));
    } else {
      this.subscribeToSaveResponse(this.sensorService.create(sensor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISensor>>): void {
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

  protected updateForm(sensor: ISensor): void {
    this.sensor = sensor;
    this.sensorFormService.resetForm(this.editForm, sensor);

    this.configuracaoAlertasSharedCollection =
      this.configuracaoAlertaService.addConfiguracaoAlertaToCollectionIfMissing<IConfiguracaoAlerta>(
        this.configuracaoAlertasSharedCollection,
        sensor.configuracaoAlertas,
      );
    this.dadoSensorsSharedCollection = this.dadoSensorService.addDadoSensorToCollectionIfMissing<IDadoSensor>(
      this.dadoSensorsSharedCollection,
      sensor.dadoSensores,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.configuracaoAlertaService
      .query()
      .pipe(map((res: HttpResponse<IConfiguracaoAlerta[]>) => res.body ?? []))
      .pipe(
        map((configuracaoAlertas: IConfiguracaoAlerta[]) =>
          this.configuracaoAlertaService.addConfiguracaoAlertaToCollectionIfMissing<IConfiguracaoAlerta>(
            configuracaoAlertas,
            this.sensor?.configuracaoAlertas,
          ),
        ),
      )
      .subscribe((configuracaoAlertas: IConfiguracaoAlerta[]) => (this.configuracaoAlertasSharedCollection = configuracaoAlertas));

    this.dadoSensorService
      .query()
      .pipe(map((res: HttpResponse<IDadoSensor[]>) => res.body ?? []))
      .pipe(
        map((dadoSensors: IDadoSensor[]) =>
          this.dadoSensorService.addDadoSensorToCollectionIfMissing<IDadoSensor>(dadoSensors, this.sensor?.dadoSensores),
        ),
      )
      .subscribe((dadoSensors: IDadoSensor[]) => (this.dadoSensorsSharedCollection = dadoSensors));
  }
}
