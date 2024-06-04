import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { EnrollmentService } from '../service/enrollment.service';
import { IEnrollment } from '../enrollment.model';
import { EnrollmentFormService } from './enrollment-form.service';

import { EnrollmentUpdateComponent } from './enrollment-update.component';

describe('Enrollment Management Update Component', () => {
  let comp: EnrollmentUpdateComponent;
  let fixture: ComponentFixture<EnrollmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enrollmentFormService: EnrollmentFormService;
  let enrollmentService: EnrollmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, EnrollmentUpdateComponent],
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
      .overrideTemplate(EnrollmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnrollmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enrollmentFormService = TestBed.inject(EnrollmentFormService);
    enrollmentService = TestBed.inject(EnrollmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const enrollment: IEnrollment = { id: 456 };

      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      expect(comp.enrollment).toEqual(enrollment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrollment>>();
      const enrollment = { id: 123 };
      jest.spyOn(enrollmentFormService, 'getEnrollment').mockReturnValue(enrollment);
      jest.spyOn(enrollmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enrollment }));
      saveSubject.complete();

      // THEN
      expect(enrollmentFormService.getEnrollment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enrollmentService.update).toHaveBeenCalledWith(expect.objectContaining(enrollment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrollment>>();
      const enrollment = { id: 123 };
      jest.spyOn(enrollmentFormService, 'getEnrollment').mockReturnValue({ id: null });
      jest.spyOn(enrollmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrollment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enrollment }));
      saveSubject.complete();

      // THEN
      expect(enrollmentFormService.getEnrollment).toHaveBeenCalled();
      expect(enrollmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnrollment>>();
      const enrollment = { id: 123 };
      jest.spyOn(enrollmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enrollment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enrollmentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
