import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';
import { ClienteService } from '../service/cliente.service';
import { ICliente } from '../cliente.model';
import { ClienteFormService } from './cliente-form.service';

import { ClienteUpdateComponent } from './cliente-update.component';

describe('Cliente Management Update Component', () => {
  let comp: ClienteUpdateComponent;
  let fixture: ComponentFixture<ClienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clienteFormService: ClienteFormService;
  let clienteService: ClienteService;
  let sensorService: SensorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ClienteUpdateComponent],
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
      .overrideTemplate(ClienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clienteFormService = TestBed.inject(ClienteFormService);
    clienteService = TestBed.inject(ClienteService);
    sensorService = TestBed.inject(SensorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sensor query and add missing value', () => {
      const cliente: ICliente = { id: 456 };
      const sensores: ISensor = { id: 3966 };
      cliente.sensores = sensores;

      const sensorCollection: ISensor[] = [{ id: 15649 }];
      jest.spyOn(sensorService, 'query').mockReturnValue(of(new HttpResponse({ body: sensorCollection })));
      const additionalSensors = [sensores];
      const expectedCollection: ISensor[] = [...additionalSensors, ...sensorCollection];
      jest.spyOn(sensorService, 'addSensorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(sensorService.query).toHaveBeenCalled();
      expect(sensorService.addSensorToCollectionIfMissing).toHaveBeenCalledWith(
        sensorCollection,
        ...additionalSensors.map(expect.objectContaining),
      );
      expect(comp.sensorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cliente: ICliente = { id: 456 };
      const sensores: ISensor = { id: 23327 };
      cliente.sensores = sensores;

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(comp.sensorsSharedCollection).toContain(sensores);
      expect(comp.cliente).toEqual(cliente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteFormService, 'getCliente').mockReturnValue(cliente);
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(clienteFormService.getCliente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clienteService.update).toHaveBeenCalledWith(expect.objectContaining(cliente));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteFormService, 'getCliente').mockReturnValue({ id: null });
      jest.spyOn(clienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(clienteFormService.getCliente).toHaveBeenCalled();
      expect(clienteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 123 };
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clienteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSensor', () => {
      it('Should forward to sensorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sensorService, 'compareSensor');
        comp.compareSensor(entity, entity2);
        expect(sensorService.compareSensor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
