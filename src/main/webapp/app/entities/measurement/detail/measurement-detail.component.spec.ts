import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MeasurementDetailComponent } from './measurement-detail.component';

describe('Measurement Management Detail Component', () => {
  let comp: MeasurementDetailComponent;
  let fixture: ComponentFixture<MeasurementDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeasurementDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MeasurementDetailComponent,
              resolve: { measurement: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MeasurementDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MeasurementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load measurement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MeasurementDetailComponent);

      // THEN
      expect(instance.measurement()).toEqual(expect.objectContaining({ id: 123 }));
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
