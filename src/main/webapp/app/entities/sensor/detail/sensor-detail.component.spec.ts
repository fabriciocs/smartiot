import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SensorDetailComponent } from './sensor-detail.component';

describe('Sensor Management Detail Component', () => {
  let comp: SensorDetailComponent;
  let fixture: ComponentFixture<SensorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SensorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SensorDetailComponent,
              resolve: { sensor: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SensorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SensorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sensor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SensorDetailComponent);

      // THEN
      expect(instance.sensor()).toEqual(expect.objectContaining({ id: 123 }));
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
