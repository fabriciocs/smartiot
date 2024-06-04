import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AggregatedDataDetailComponent } from './aggregated-data-detail.component';

describe('AggregatedData Management Detail Component', () => {
  let comp: AggregatedDataDetailComponent;
  let fixture: ComponentFixture<AggregatedDataDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AggregatedDataDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AggregatedDataDetailComponent,
              resolve: { aggregatedData: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AggregatedDataDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AggregatedDataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aggregatedData on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AggregatedDataDetailComponent);

      // THEN
      expect(instance.aggregatedData()).toEqual(expect.objectContaining({ id: 123 }));
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
