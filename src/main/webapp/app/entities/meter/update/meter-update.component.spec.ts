import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { MeterService } from '../service/meter.service';
import { IMeter } from '../meter.model';
import { MeterFormService } from './meter-form.service';

import { MeterUpdateComponent } from './meter-update.component';

describe('Meter Management Update Component', () => {
  let comp: MeterUpdateComponent;
  let fixture: ComponentFixture<MeterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let meterFormService: MeterFormService;
  let meterService: MeterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MeterUpdateComponent],
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
      .overrideTemplate(MeterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    meterFormService = TestBed.inject(MeterFormService);
    meterService = TestBed.inject(MeterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const meter: IMeter = { id: 456 };

      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      expect(comp.meter).toEqual(meter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeter>>();
      const meter = { id: 123 };
      jest.spyOn(meterFormService, 'getMeter').mockReturnValue(meter);
      jest.spyOn(meterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meter }));
      saveSubject.complete();

      // THEN
      expect(meterFormService.getMeter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(meterService.update).toHaveBeenCalledWith(expect.objectContaining(meter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeter>>();
      const meter = { id: 123 };
      jest.spyOn(meterFormService, 'getMeter').mockReturnValue({ id: null });
      jest.spyOn(meterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meter }));
      saveSubject.complete();

      // THEN
      expect(meterFormService.getMeter).toHaveBeenCalled();
      expect(meterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeter>>();
      const meter = { id: 123 };
      jest.spyOn(meterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(meterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
