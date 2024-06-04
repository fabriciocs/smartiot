import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEnrollment } from 'app/entities/enrollment/enrollment.model';
import { EnrollmentService } from 'app/entities/enrollment/service/enrollment.service';
import { MeasurementService } from '../service/measurement.service';
import { IMeasurement } from '../measurement.model';
import { MeasurementFormService } from './measurement-form.service';

import { MeasurementUpdateComponent } from './measurement-update.component';

describe('Measurement Management Update Component', () => {
  let comp: MeasurementUpdateComponent;
  let fixture: ComponentFixture<MeasurementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let measurementFormService: MeasurementFormService;
  let measurementService: MeasurementService;
  let enrollmentService: EnrollmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MeasurementUpdateComponent],
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
      .overrideTemplate(MeasurementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeasurementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    measurementFormService = TestBed.inject(MeasurementFormService);
    measurementService = TestBed.inject(MeasurementService);
    enrollmentService = TestBed.inject(EnrollmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Enrollment query and add missing value', () => {
      const measurement: IMeasurement = { id: 456 };
      const enrollment: IEnrollment = { id: 25787 };
      measurement.enrollment = enrollment;

      const enrollmentCollection: IEnrollment[] = [{ id: 31760 }];
      jest.spyOn(enrollmentService, 'query').mockReturnValue(of(new HttpResponse({ body: enrollmentCollection })));
      const additionalEnrollments = [enrollment];
      const expectedCollection: IEnrollment[] = [...additionalEnrollments, ...enrollmentCollection];
      jest.spyOn(enrollmentService, 'addEnrollmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ measurement });
      comp.ngOnInit();

      expect(enrollmentService.query).toHaveBeenCalled();
      expect(enrollmentService.addEnrollmentToCollectionIfMissing).toHaveBeenCalledWith(
        enrollmentCollection,
        ...additionalEnrollments.map(expect.objectContaining),
      );
      expect(comp.enrollmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const measurement: IMeasurement = { id: 456 };
      const enrollment: IEnrollment = { id: 28780 };
      measurement.enrollment = enrollment;

      activatedRoute.data = of({ measurement });
      comp.ngOnInit();

      expect(comp.enrollmentsSharedCollection).toContain(enrollment);
      expect(comp.measurement).toEqual(measurement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeasurement>>();
      const measurement = { id: 123 };
      jest.spyOn(measurementFormService, 'getMeasurement').mockReturnValue(measurement);
      jest.spyOn(measurementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measurement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: measurement }));
      saveSubject.complete();

      // THEN
      expect(measurementFormService.getMeasurement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(measurementService.update).toHaveBeenCalledWith(expect.objectContaining(measurement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeasurement>>();
      const measurement = { id: 123 };
      jest.spyOn(measurementFormService, 'getMeasurement').mockReturnValue({ id: null });
      jest.spyOn(measurementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measurement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: measurement }));
      saveSubject.complete();

      // THEN
      expect(measurementFormService.getMeasurement).toHaveBeenCalled();
      expect(measurementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMeasurement>>();
      const measurement = { id: 123 };
      jest.spyOn(measurementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ measurement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(measurementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEnrollment', () => {
      it('Should forward to enrollmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(enrollmentService, 'compareEnrollment');
        comp.compareEnrollment(entity, entity2);
        expect(enrollmentService.compareEnrollment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
