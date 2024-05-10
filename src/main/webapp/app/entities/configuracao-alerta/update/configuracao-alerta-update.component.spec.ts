import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ISensor } from 'app/entities/sensor/sensor.model';
import { SensorService } from 'app/entities/sensor/service/sensor.service';
import { ConfiguracaoAlertaService } from '../service/configuracao-alerta.service';
import { IConfiguracaoAlerta } from '../configuracao-alerta.model';
import { ConfiguracaoAlertaFormService } from './configuracao-alerta-form.service';

import { ConfiguracaoAlertaUpdateComponent } from './configuracao-alerta-update.component';

describe('ConfiguracaoAlerta Management Update Component', () => {
  let comp: ConfiguracaoAlertaUpdateComponent;
  let fixture: ComponentFixture<ConfiguracaoAlertaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let configuracaoAlertaFormService: ConfiguracaoAlertaFormService;
  let configuracaoAlertaService: ConfiguracaoAlertaService;
  let sensorService: SensorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ConfiguracaoAlertaUpdateComponent],
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
      .overrideTemplate(ConfiguracaoAlertaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConfiguracaoAlertaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    configuracaoAlertaFormService = TestBed.inject(ConfiguracaoAlertaFormService);
    configuracaoAlertaService = TestBed.inject(ConfiguracaoAlertaService);
    sensorService = TestBed.inject(SensorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sensor query and add missing value', () => {
      const configuracaoAlerta: IConfiguracaoAlerta = { id: 456 };
      const sensor: ISensor = { id: 5172 };
      configuracaoAlerta.sensor = sensor;

      const sensorCollection: ISensor[] = [{ id: 1926 }];
      jest.spyOn(sensorService, 'query').mockReturnValue(of(new HttpResponse({ body: sensorCollection })));
      const additionalSensors = [sensor];
      const expectedCollection: ISensor[] = [...additionalSensors, ...sensorCollection];
      jest.spyOn(sensorService, 'addSensorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ configuracaoAlerta });
      comp.ngOnInit();

      expect(sensorService.query).toHaveBeenCalled();
      expect(sensorService.addSensorToCollectionIfMissing).toHaveBeenCalledWith(
        sensorCollection,
        ...additionalSensors.map(expect.objectContaining),
      );
      expect(comp.sensorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const configuracaoAlerta: IConfiguracaoAlerta = { id: 456 };
      const sensor: ISensor = { id: 10021 };
      configuracaoAlerta.sensor = sensor;

      activatedRoute.data = of({ configuracaoAlerta });
      comp.ngOnInit();

      expect(comp.sensorsSharedCollection).toContain(sensor);
      expect(comp.configuracaoAlerta).toEqual(configuracaoAlerta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfiguracaoAlerta>>();
      const configuracaoAlerta = { id: 123 };
      jest.spyOn(configuracaoAlertaFormService, 'getConfiguracaoAlerta').mockReturnValue(configuracaoAlerta);
      jest.spyOn(configuracaoAlertaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configuracaoAlerta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configuracaoAlerta }));
      saveSubject.complete();

      // THEN
      expect(configuracaoAlertaFormService.getConfiguracaoAlerta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(configuracaoAlertaService.update).toHaveBeenCalledWith(expect.objectContaining(configuracaoAlerta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfiguracaoAlerta>>();
      const configuracaoAlerta = { id: 123 };
      jest.spyOn(configuracaoAlertaFormService, 'getConfiguracaoAlerta').mockReturnValue({ id: null });
      jest.spyOn(configuracaoAlertaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configuracaoAlerta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configuracaoAlerta }));
      saveSubject.complete();

      // THEN
      expect(configuracaoAlertaFormService.getConfiguracaoAlerta).toHaveBeenCalled();
      expect(configuracaoAlertaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConfiguracaoAlerta>>();
      const configuracaoAlerta = { id: 123 };
      jest.spyOn(configuracaoAlertaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configuracaoAlerta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(configuracaoAlertaService.update).toHaveBeenCalled();
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
