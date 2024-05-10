import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { DadoSensorService } from '../service/dado-sensor.service';
import { IDadoSensor } from '../dado-sensor.model';
import { DadoSensorFormService } from './dado-sensor-form.service';

import { DadoSensorUpdateComponent } from './dado-sensor-update.component';

describe('DadoSensor Management Update Component', () => {
  let comp: DadoSensorUpdateComponent;
  let fixture: ComponentFixture<DadoSensorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dadoSensorFormService: DadoSensorFormService;
  let dadoSensorService: DadoSensorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, DadoSensorUpdateComponent],
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
      .overrideTemplate(DadoSensorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DadoSensorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dadoSensorFormService = TestBed.inject(DadoSensorFormService);
    dadoSensorService = TestBed.inject(DadoSensorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dadoSensor: IDadoSensor = { id: 456 };

      activatedRoute.data = of({ dadoSensor });
      comp.ngOnInit();

      expect(comp.dadoSensor).toEqual(dadoSensor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDadoSensor>>();
      const dadoSensor = { id: 123 };
      jest.spyOn(dadoSensorFormService, 'getDadoSensor').mockReturnValue(dadoSensor);
      jest.spyOn(dadoSensorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dadoSensor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dadoSensor }));
      saveSubject.complete();

      // THEN
      expect(dadoSensorFormService.getDadoSensor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dadoSensorService.update).toHaveBeenCalledWith(expect.objectContaining(dadoSensor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDadoSensor>>();
      const dadoSensor = { id: 123 };
      jest.spyOn(dadoSensorFormService, 'getDadoSensor').mockReturnValue({ id: null });
      jest.spyOn(dadoSensorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dadoSensor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dadoSensor }));
      saveSubject.complete();

      // THEN
      expect(dadoSensorFormService.getDadoSensor).toHaveBeenCalled();
      expect(dadoSensorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDadoSensor>>();
      const dadoSensor = { id: 123 };
      jest.spyOn(dadoSensorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dadoSensor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dadoSensorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
