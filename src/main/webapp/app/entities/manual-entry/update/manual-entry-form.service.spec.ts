import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../manual-entry.test-samples';

import { ManualEntryFormService } from './manual-entry-form.service';

describe('ManualEntry Form Service', () => {
  let service: ManualEntryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManualEntryFormService);
  });

  describe('Service methods', () => {
    describe('createManualEntryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createManualEntryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            entryType: expect.any(Object),
            value: expect.any(Object),
            entryDate: expect.any(Object),
          }),
        );
      });

      it('passing IManualEntry should create a new form with FormGroup', () => {
        const formGroup = service.createManualEntryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            entryType: expect.any(Object),
            value: expect.any(Object),
            entryDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getManualEntry', () => {
      it('should return NewManualEntry for default ManualEntry initial value', () => {
        const formGroup = service.createManualEntryFormGroup(sampleWithNewData);

        const manualEntry = service.getManualEntry(formGroup) as any;

        expect(manualEntry).toMatchObject(sampleWithNewData);
      });

      it('should return NewManualEntry for empty ManualEntry initial value', () => {
        const formGroup = service.createManualEntryFormGroup();

        const manualEntry = service.getManualEntry(formGroup) as any;

        expect(manualEntry).toMatchObject({});
      });

      it('should return IManualEntry', () => {
        const formGroup = service.createManualEntryFormGroup(sampleWithRequiredData);

        const manualEntry = service.getManualEntry(formGroup) as any;

        expect(manualEntry).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IManualEntry should not enable id FormControl', () => {
        const formGroup = service.createManualEntryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewManualEntry should disable id FormControl', () => {
        const formGroup = service.createManualEntryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
