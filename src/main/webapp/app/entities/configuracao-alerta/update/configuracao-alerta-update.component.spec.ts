import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const configuracaoAlerta: IConfiguracaoAlerta = { id: 456 };

      activatedRoute.data = of({ configuracaoAlerta });
      comp.ngOnInit();

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
});
