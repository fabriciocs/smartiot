import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IConsumer } from 'app/entities/consumer/consumer.model';
import { ConsumerService } from 'app/entities/consumer/service/consumer.service';
import { AlertService } from '../service/alert.service';
import { IAlert } from '../alert.model';
import { AlertFormService } from './alert-form.service';

import { AlertUpdateComponent } from './alert-update.component';

describe('Alert Management Update Component', () => {
  let comp: AlertUpdateComponent;
  let fixture: ComponentFixture<AlertUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alertFormService: AlertFormService;
  let alertService: AlertService;
  let consumerService: ConsumerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AlertUpdateComponent],
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
      .overrideTemplate(AlertUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlertUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alertFormService = TestBed.inject(AlertFormService);
    alertService = TestBed.inject(AlertService);
    consumerService = TestBed.inject(ConsumerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Consumer query and add missing value', () => {
      const alert: IAlert = { id: 456 };
      const consumer: IConsumer = { id: 19383 };
      alert.consumer = consumer;

      const consumerCollection: IConsumer[] = [{ id: 8661 }];
      jest.spyOn(consumerService, 'query').mockReturnValue(of(new HttpResponse({ body: consumerCollection })));
      const additionalConsumers = [consumer];
      const expectedCollection: IConsumer[] = [...additionalConsumers, ...consumerCollection];
      jest.spyOn(consumerService, 'addConsumerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alert });
      comp.ngOnInit();

      expect(consumerService.query).toHaveBeenCalled();
      expect(consumerService.addConsumerToCollectionIfMissing).toHaveBeenCalledWith(
        consumerCollection,
        ...additionalConsumers.map(expect.objectContaining),
      );
      expect(comp.consumersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const alert: IAlert = { id: 456 };
      const consumer: IConsumer = { id: 25560 };
      alert.consumer = consumer;

      activatedRoute.data = of({ alert });
      comp.ngOnInit();

      expect(comp.consumersSharedCollection).toContain(consumer);
      expect(comp.alert).toEqual(alert);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlert>>();
      const alert = { id: 123 };
      jest.spyOn(alertFormService, 'getAlert').mockReturnValue(alert);
      jest.spyOn(alertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alert }));
      saveSubject.complete();

      // THEN
      expect(alertFormService.getAlert).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alertService.update).toHaveBeenCalledWith(expect.objectContaining(alert));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlert>>();
      const alert = { id: 123 };
      jest.spyOn(alertFormService, 'getAlert').mockReturnValue({ id: null });
      jest.spyOn(alertService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alert: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alert }));
      saveSubject.complete();

      // THEN
      expect(alertFormService.getAlert).toHaveBeenCalled();
      expect(alertService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlert>>();
      const alert = { id: 123 };
      jest.spyOn(alertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alertService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareConsumer', () => {
      it('Should forward to consumerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(consumerService, 'compareConsumer');
        comp.compareConsumer(entity, entity2);
        expect(consumerService.compareConsumer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
