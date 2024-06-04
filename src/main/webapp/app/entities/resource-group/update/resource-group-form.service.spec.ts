import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../resource-group.test-samples';

import { ResourceGroupFormService } from './resource-group-form.service';

describe('ResourceGroup Form Service', () => {
  let service: ResourceGroupFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResourceGroupFormService);
  });

  describe('Service methods', () => {
    describe('createResourceGroupFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createResourceGroupFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IResourceGroup should create a new form with FormGroup', () => {
        const formGroup = service.createResourceGroupFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getResourceGroup', () => {
      it('should return NewResourceGroup for default ResourceGroup initial value', () => {
        const formGroup = service.createResourceGroupFormGroup(sampleWithNewData);

        const resourceGroup = service.getResourceGroup(formGroup) as any;

        expect(resourceGroup).toMatchObject(sampleWithNewData);
      });

      it('should return NewResourceGroup for empty ResourceGroup initial value', () => {
        const formGroup = service.createResourceGroupFormGroup();

        const resourceGroup = service.getResourceGroup(formGroup) as any;

        expect(resourceGroup).toMatchObject({});
      });

      it('should return IResourceGroup', () => {
        const formGroup = service.createResourceGroupFormGroup(sampleWithRequiredData);

        const resourceGroup = service.getResourceGroup(formGroup) as any;

        expect(resourceGroup).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IResourceGroup should not enable id FormControl', () => {
        const formGroup = service.createResourceGroupFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewResourceGroup should disable id FormControl', () => {
        const formGroup = service.createResourceGroupFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
