import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { SysUserService } from '../service/sys-user.service';
import { ISysUser } from '../sys-user.model';
import { SysUserFormService } from './sys-user-form.service';

import { SysUserUpdateComponent } from './sys-user-update.component';

describe('SysUser Management Update Component', () => {
  let comp: SysUserUpdateComponent;
  let fixture: ComponentFixture<SysUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sysUserFormService: SysUserFormService;
  let sysUserService: SysUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SysUserUpdateComponent],
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
      .overrideTemplate(SysUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SysUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sysUserFormService = TestBed.inject(SysUserFormService);
    sysUserService = TestBed.inject(SysUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const sysUser: ISysUser = { id: 456 };

      activatedRoute.data = of({ sysUser });
      comp.ngOnInit();

      expect(comp.sysUser).toEqual(sysUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISysUser>>();
      const sysUser = { id: 123 };
      jest.spyOn(sysUserFormService, 'getSysUser').mockReturnValue(sysUser);
      jest.spyOn(sysUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sysUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sysUser }));
      saveSubject.complete();

      // THEN
      expect(sysUserFormService.getSysUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sysUserService.update).toHaveBeenCalledWith(expect.objectContaining(sysUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISysUser>>();
      const sysUser = { id: 123 };
      jest.spyOn(sysUserFormService, 'getSysUser').mockReturnValue({ id: null });
      jest.spyOn(sysUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sysUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sysUser }));
      saveSubject.complete();

      // THEN
      expect(sysUserFormService.getSysUser).toHaveBeenCalled();
      expect(sysUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISysUser>>();
      const sysUser = { id: 123 };
      jest.spyOn(sysUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sysUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sysUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
