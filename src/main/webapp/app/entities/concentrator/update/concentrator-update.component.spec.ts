import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ConcentratorService } from '../service/concentrator.service';
import { IConcentrator } from '../concentrator.model';
import { ConcentratorFormService } from './concentrator-form.service';

import { ConcentratorUpdateComponent } from './concentrator-update.component';

describe('Concentrator Management Update Component', () => {
  let comp: ConcentratorUpdateComponent;
  let fixture: ComponentFixture<ConcentratorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let concentratorFormService: ConcentratorFormService;
  let concentratorService: ConcentratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ConcentratorUpdateComponent],
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
      .overrideTemplate(ConcentratorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConcentratorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    concentratorFormService = TestBed.inject(ConcentratorFormService);
    concentratorService = TestBed.inject(ConcentratorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const concentrator: IConcentrator = { id: 456 };

      activatedRoute.data = of({ concentrator });
      comp.ngOnInit();

      expect(comp.concentrator).toEqual(concentrator);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConcentrator>>();
      const concentrator = { id: 123 };
      jest.spyOn(concentratorFormService, 'getConcentrator').mockReturnValue(concentrator);
      jest.spyOn(concentratorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concentrator });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: concentrator }));
      saveSubject.complete();

      // THEN
      expect(concentratorFormService.getConcentrator).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(concentratorService.update).toHaveBeenCalledWith(expect.objectContaining(concentrator));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConcentrator>>();
      const concentrator = { id: 123 };
      jest.spyOn(concentratorFormService, 'getConcentrator').mockReturnValue({ id: null });
      jest.spyOn(concentratorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concentrator: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: concentrator }));
      saveSubject.complete();

      // THEN
      expect(concentratorFormService.getConcentrator).toHaveBeenCalled();
      expect(concentratorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConcentrator>>();
      const concentrator = { id: 123 };
      jest.spyOn(concentratorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concentrator });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(concentratorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
