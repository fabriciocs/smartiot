import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { PricingService } from '../service/pricing.service';
import { IPricing } from '../pricing.model';
import { PricingFormService } from './pricing-form.service';

import { PricingUpdateComponent } from './pricing-update.component';

describe('Pricing Management Update Component', () => {
  let comp: PricingUpdateComponent;
  let fixture: ComponentFixture<PricingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pricingFormService: PricingFormService;
  let pricingService: PricingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PricingUpdateComponent],
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
      .overrideTemplate(PricingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PricingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pricingFormService = TestBed.inject(PricingFormService);
    pricingService = TestBed.inject(PricingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pricing: IPricing = { id: 456 };

      activatedRoute.data = of({ pricing });
      comp.ngOnInit();

      expect(comp.pricing).toEqual(pricing);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPricing>>();
      const pricing = { id: 123 };
      jest.spyOn(pricingFormService, 'getPricing').mockReturnValue(pricing);
      jest.spyOn(pricingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pricing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pricing }));
      saveSubject.complete();

      // THEN
      expect(pricingFormService.getPricing).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pricingService.update).toHaveBeenCalledWith(expect.objectContaining(pricing));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPricing>>();
      const pricing = { id: 123 };
      jest.spyOn(pricingFormService, 'getPricing').mockReturnValue({ id: null });
      jest.spyOn(pricingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pricing: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pricing }));
      saveSubject.complete();

      // THEN
      expect(pricingFormService.getPricing).toHaveBeenCalled();
      expect(pricingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPricing>>();
      const pricing = { id: 123 };
      jest.spyOn(pricingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pricing });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pricingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
