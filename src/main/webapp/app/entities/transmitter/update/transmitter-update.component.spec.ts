import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { TransmitterService } from '../service/transmitter.service';
import { ITransmitter } from '../transmitter.model';
import { TransmitterFormService } from './transmitter-form.service';

import { TransmitterUpdateComponent } from './transmitter-update.component';

describe('Transmitter Management Update Component', () => {
  let comp: TransmitterUpdateComponent;
  let fixture: ComponentFixture<TransmitterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transmitterFormService: TransmitterFormService;
  let transmitterService: TransmitterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TransmitterUpdateComponent],
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
      .overrideTemplate(TransmitterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransmitterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transmitterFormService = TestBed.inject(TransmitterFormService);
    transmitterService = TestBed.inject(TransmitterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const transmitter: ITransmitter = { id: 456 };

      activatedRoute.data = of({ transmitter });
      comp.ngOnInit();

      expect(comp.transmitter).toEqual(transmitter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransmitter>>();
      const transmitter = { id: 123 };
      jest.spyOn(transmitterFormService, 'getTransmitter').mockReturnValue(transmitter);
      jest.spyOn(transmitterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transmitter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transmitter }));
      saveSubject.complete();

      // THEN
      expect(transmitterFormService.getTransmitter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transmitterService.update).toHaveBeenCalledWith(expect.objectContaining(transmitter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransmitter>>();
      const transmitter = { id: 123 };
      jest.spyOn(transmitterFormService, 'getTransmitter').mockReturnValue({ id: null });
      jest.spyOn(transmitterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transmitter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transmitter }));
      saveSubject.complete();

      // THEN
      expect(transmitterFormService.getTransmitter).toHaveBeenCalled();
      expect(transmitterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransmitter>>();
      const transmitter = { id: 123 };
      jest.spyOn(transmitterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transmitter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transmitterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
