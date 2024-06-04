import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { AggregatedDataService } from '../service/aggregated-data.service';
import { IAggregatedData } from '../aggregated-data.model';
import { AggregatedDataFormService } from './aggregated-data-form.service';

import { AggregatedDataUpdateComponent } from './aggregated-data-update.component';

describe('AggregatedData Management Update Component', () => {
  let comp: AggregatedDataUpdateComponent;
  let fixture: ComponentFixture<AggregatedDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aggregatedDataFormService: AggregatedDataFormService;
  let aggregatedDataService: AggregatedDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AggregatedDataUpdateComponent],
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
      .overrideTemplate(AggregatedDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AggregatedDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aggregatedDataFormService = TestBed.inject(AggregatedDataFormService);
    aggregatedDataService = TestBed.inject(AggregatedDataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aggregatedData: IAggregatedData = { id: 456 };

      activatedRoute.data = of({ aggregatedData });
      comp.ngOnInit();

      expect(comp.aggregatedData).toEqual(aggregatedData);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAggregatedData>>();
      const aggregatedData = { id: 123 };
      jest.spyOn(aggregatedDataFormService, 'getAggregatedData').mockReturnValue(aggregatedData);
      jest.spyOn(aggregatedDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aggregatedData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aggregatedData }));
      saveSubject.complete();

      // THEN
      expect(aggregatedDataFormService.getAggregatedData).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aggregatedDataService.update).toHaveBeenCalledWith(expect.objectContaining(aggregatedData));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAggregatedData>>();
      const aggregatedData = { id: 123 };
      jest.spyOn(aggregatedDataFormService, 'getAggregatedData').mockReturnValue({ id: null });
      jest.spyOn(aggregatedDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aggregatedData: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aggregatedData }));
      saveSubject.complete();

      // THEN
      expect(aggregatedDataFormService.getAggregatedData).toHaveBeenCalled();
      expect(aggregatedDataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAggregatedData>>();
      const aggregatedData = { id: 123 };
      jest.spyOn(aggregatedDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aggregatedData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aggregatedDataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
