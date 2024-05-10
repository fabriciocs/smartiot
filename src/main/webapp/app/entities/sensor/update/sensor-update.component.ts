import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
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

  clientesSharedCollection: ICliente[] = [];
  dadoSensorsSharedCollection: IDadoSensor[] = [];

  protected sensorService = inject(SensorService);
  protected sensorFormService = inject(SensorFormService);
  protected clienteService = inject(ClienteService);
  protected dadoSensorService = inject(DadoSensorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SensorFormGroup = this.sensorFormService.createSensorFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

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

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      sensor.cliente,
    );
    this.dadoSensorsSharedCollection = this.dadoSensorService.addDadoSensorToCollectionIfMissing<IDadoSensor>(
      this.dadoSensorsSharedCollection,
      sensor.dadoSensores,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.sensor?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

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
