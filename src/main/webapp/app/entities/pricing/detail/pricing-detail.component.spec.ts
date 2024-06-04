import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PricingDetailComponent } from './pricing-detail.component';

describe('Pricing Management Detail Component', () => {
  let comp: PricingDetailComponent;
  let fixture: ComponentFixture<PricingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PricingDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PricingDetailComponent,
              resolve: { pricing: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PricingDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PricingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pricing on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PricingDetailComponent);

      // THEN
      expect(instance.pricing()).toEqual(expect.objectContaining({ id: 123 }));
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
