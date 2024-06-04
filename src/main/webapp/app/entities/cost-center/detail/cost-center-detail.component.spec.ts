import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CostCenterDetailComponent } from './cost-center-detail.component';

describe('CostCenter Management Detail Component', () => {
  let comp: CostCenterDetailComponent;
  let fixture: ComponentFixture<CostCenterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CostCenterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CostCenterDetailComponent,
              resolve: { costCenter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CostCenterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CostCenterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load costCenter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CostCenterDetailComponent);

      // THEN
      expect(instance.costCenter()).toEqual(expect.objectContaining({ id: 123 }));
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
