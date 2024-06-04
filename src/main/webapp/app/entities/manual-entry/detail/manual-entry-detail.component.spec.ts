import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ManualEntryDetailComponent } from './manual-entry-detail.component';

describe('ManualEntry Management Detail Component', () => {
  let comp: ManualEntryDetailComponent;
  let fixture: ComponentFixture<ManualEntryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManualEntryDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ManualEntryDetailComponent,
              resolve: { manualEntry: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ManualEntryDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManualEntryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load manualEntry on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ManualEntryDetailComponent);

      // THEN
      expect(instance.manualEntry()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
