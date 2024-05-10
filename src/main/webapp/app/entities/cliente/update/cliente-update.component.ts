import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';
import { ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { ClienteFormService, ClienteFormGroup } from './cliente-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;
  cliente: ICliente | null = null;

  sensorsSharedCollection: ISensor[] = [];

  protected clienteService = inject(ClienteService);
  protected clienteFormService = inject(ClienteFormService);
  protected sensorService = inject(SensorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClienteFormGroup = this.clienteFormService.createClienteFormGroup();

  compareSensor = (o1: ISensor | null, o2: ISensor | null): boolean => this.sensorService.compareSensor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.cliente = cliente;
      if (cliente) {
        this.updateForm(cliente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.clienteFormService.getCliente(this.editForm);
    if (cliente.id !== null) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.cliente = cliente;
    this.clienteFormService.resetForm(this.editForm, cliente);

    this.sensorsSharedCollection = this.sensorService.addSensorToCollectionIfMissing<ISensor>(
      this.sensorsSharedCollection,
      cliente.sensores,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sensorService
      .query()
      .pipe(map((res: HttpResponse<ISensor[]>) => res.body ?? []))
      .pipe(map((sensors: ISensor[]) => this.sensorService.addSensorToCollectionIfMissing<ISensor>(sensors, this.cliente?.sensores)))
      .subscribe((sensors: ISensor[]) => (this.sensorsSharedCollection = sensors));
  }
}
