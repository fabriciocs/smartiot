import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ResourceGroupService } from '../service/resource-group.service';
import { IResourceGroup } from '../resource-group.model';
import { ResourceGroupFormService } from './resource-group-form.service';

import { ResourceGroupUpdateComponent } from './resource-group-update.component';

describe('ResourceGroup Management Update Component', () => {
  let comp: ResourceGroupUpdateComponent;
  let fixture: ComponentFixture<ResourceGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resourceGroupFormService: ResourceGroupFormService;
  let resourceGroupService: ResourceGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ResourceGroupUpdateComponent],
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
      .overrideTemplate(ResourceGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResourceGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resourceGroupFormService = TestBed.inject(ResourceGroupFormService);
    resourceGroupService = TestBed.inject(ResourceGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const resourceGroup: IResourceGroup = { id: 456 };

      activatedRoute.data = of({ resourceGroup });
      comp.ngOnInit();

      expect(comp.resourceGroup).toEqual(resourceGroup);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceGroup>>();
      const resourceGroup = { id: 123 };
      jest.spyOn(resourceGroupFormService, 'getResourceGroup').mockReturnValue(resourceGroup);
      jest.spyOn(resourceGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceGroup }));
      saveSubject.complete();

      // THEN
      expect(resourceGroupFormService.getResourceGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resourceGroupService.update).toHaveBeenCalledWith(expect.objectContaining(resourceGroup));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceGroup>>();
      const resourceGroup = { id: 123 };
      jest.spyOn(resourceGroupFormService, 'getResourceGroup').mockReturnValue({ id: null });
      jest.spyOn(resourceGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resourceGroup }));
      saveSubject.complete();

      // THEN
      expect(resourceGroupFormService.getResourceGroup).toHaveBeenCalled();
      expect(resourceGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResourceGroup>>();
      const resourceGroup = { id: 123 };
      jest.spyOn(resourceGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resourceGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resourceGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
