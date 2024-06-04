import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { RepeaterService } from '../service/repeater.service';
import { IRepeater } from '../repeater.model';
import { RepeaterFormService } from './repeater-form.service';

import { RepeaterUpdateComponent } from './repeater-update.component';

describe('Repeater Management Update Component', () => {
  let comp: RepeaterUpdateComponent;
  let fixture: ComponentFixture<RepeaterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let repeaterFormService: RepeaterFormService;
  let repeaterService: RepeaterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RepeaterUpdateComponent],
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
      .overrideTemplate(RepeaterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RepeaterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    repeaterFormService = TestBed.inject(RepeaterFormService);
    repeaterService = TestBed.inject(RepeaterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const repeater: IRepeater = { id: 456 };

      activatedRoute.data = of({ repeater });
      comp.ngOnInit();

      expect(comp.repeater).toEqual(repeater);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepeater>>();
      const repeater = { id: 123 };
      jest.spyOn(repeaterFormService, 'getRepeater').mockReturnValue(repeater);
      jest.spyOn(repeaterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repeater });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: repeater }));
      saveSubject.complete();

      // THEN
      expect(repeaterFormService.getRepeater).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(repeaterService.update).toHaveBeenCalledWith(expect.objectContaining(repeater));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepeater>>();
      const repeater = { id: 123 };
      jest.spyOn(repeaterFormService, 'getRepeater').mockReturnValue({ id: null });
      jest.spyOn(repeaterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repeater: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: repeater }));
      saveSubject.complete();

      // THEN
      expect(repeaterFormService.getRepeater).toHaveBeenCalled();
      expect(repeaterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRepeater>>();
      const repeater = { id: 123 };
      jest.spyOn(repeaterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ repeater });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(repeaterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
