import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IDadoSensor } from 'app/entities/dado-sensor/dado-sensor.model';
import { DadoSensorService } from 'app/entities/dado-sensor/service/dado-sensor.service';
import { ISensor } from '../sensor.model';
import { SensorService } from '../service/sensor.service';
import { SensorFormService } from './sensor-form.service';

import { SensorUpdateComponent } from './sensor-update.component';

describe('Sensor Management Update Component', () => {
  let comp: SensorUpdateComponent;
  let fixture: ComponentFixture<SensorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sensorFormService: SensorFormService;
  let sensorService: SensorService;
  let clienteService: ClienteService;
  let dadoSensorService: DadoSensorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SensorUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SensorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SensorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sensorFormService = TestBed.inject(SensorFormService);
    sensorService = TestBed.inject(SensorService);
    clienteService = TestBed.inject(ClienteService);
    dadoSensorService = TestBed.inject(DadoSensorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const sensor: ISensor = { id: 456 };
      const cliente: ICliente = { id: 15672 };
      sensor.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 4532 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DadoSensor query and add missing value', () => {
      const sensor: ISensor = { id: 456 };
      const dadoSensores: IDadoSensor = { id: 4537 };
      sensor.dadoSensores = dadoSensores;

      const dadoSensorCollection: IDadoSensor[] = [{ id: 23499 }];
      jest.spyOn(dadoSensorService, 'query').mockReturnValue(of(new HttpResponse({ body: dadoSensorCollection })));
      const additionalDadoSensors = [dadoSensores];
      const expectedCollection: IDadoSensor[] = [...additionalDadoSensors, ...dadoSensorCollection];
      jest.spyOn(dadoSensorService, 'addDadoSensorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      expect(dadoSensorService.query).toHaveBeenCalled();
      expect(dadoSensorService.addDadoSensorToCollectionIfMissing).toHaveBeenCalledWith(
        dadoSensorCollection,
        ...additionalDadoSensors.map(expect.objectContaining),
      );
      expect(comp.dadoSensorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sensor: ISensor = { id: 456 };
      const cliente: ICliente = { id: 18137 };
      sensor.cliente = cliente;
      const dadoSensores: IDadoSensor = { id: 4321 };
      sensor.dadoSensores = dadoSensores;

      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.dadoSensorsSharedCollection).toContain(dadoSensores);
      expect(comp.sensor).toEqual(sensor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISensor>>();
      const sensor = { id: 123 };
      jest.spyOn(sensorFormService, 'getSensor').mockReturnValue(sensor);
      jest.spyOn(sensorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensor }));
      saveSubject.complete();

      // THEN
      expect(sensorFormService.getSensor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sensorService.update).toHaveBeenCalledWith(expect.objectContaining(sensor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISensor>>();
      const sensor = { id: 123 };
      jest.spyOn(sensorFormService, 'getSensor').mockReturnValue({ id: null });
      jest.spyOn(sensorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sensor }));
      saveSubject.complete();

      // THEN
      expect(sensorFormService.getSensor).toHaveBeenCalled();
      expect(sensorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISensor>>();
      const sensor = { id: 123 };
      jest.spyOn(sensorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sensor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sensorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCliente', () => {
      it('Should forward to clienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDadoSensor', () => {
      it('Should forward to dadoSensorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(dadoSensorService, 'compareDadoSensor');
        comp.compareDadoSensor(entity, entity2);
        expect(dadoSensorService.compareDadoSensor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
