import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ManualEntryService } from '../service/manual-entry.service';
import { IManualEntry } from '../manual-entry.model';
import { ManualEntryFormService } from './manual-entry-form.service';

import { ManualEntryUpdateComponent } from './manual-entry-update.component';

describe('ManualEntry Management Update Component', () => {
  let comp: ManualEntryUpdateComponent;
  let fixture: ComponentFixture<ManualEntryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let manualEntryFormService: ManualEntryFormService;
  let manualEntryService: ManualEntryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ManualEntryUpdateComponent],
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
      .overrideTemplate(ManualEntryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ManualEntryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    manualEntryFormService = TestBed.inject(ManualEntryFormService);
    manualEntryService = TestBed.inject(ManualEntryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const manualEntry: IManualEntry = { id: 456 };

      activatedRoute.data = of({ manualEntry });
      comp.ngOnInit();

      expect(comp.manualEntry).toEqual(manualEntry);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualEntry>>();
      const manualEntry = { id: 123 };
      jest.spyOn(manualEntryFormService, 'getManualEntry').mockReturnValue(manualEntry);
      jest.spyOn(manualEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualEntry }));
      saveSubject.complete();

      // THEN
      expect(manualEntryFormService.getManualEntry).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(manualEntryService.update).toHaveBeenCalledWith(expect.objectContaining(manualEntry));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualEntry>>();
      const manualEntry = { id: 123 };
      jest.spyOn(manualEntryFormService, 'getManualEntry').mockReturnValue({ id: null });
      jest.spyOn(manualEntryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualEntry: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: manualEntry }));
      saveSubject.complete();

      // THEN
      expect(manualEntryFormService.getManualEntry).toHaveBeenCalled();
      expect(manualEntryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IManualEntry>>();
      const manualEntry = { id: 123 };
      jest.spyOn(manualEntryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ manualEntry });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(manualEntryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
