import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DadoSensorDetailComponent } from './dado-sensor-detail.component';

describe('DadoSensor Management Detail Component', () => {
  let comp: DadoSensorDetailComponent;
  let fixture: ComponentFixture<DadoSensorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DadoSensorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DadoSensorDetailComponent,
              resolve: { dadoSensor: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DadoSensorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DadoSensorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dadoSensor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DadoSensorDetailComponent);

      // THEN
      expect(instance.dadoSensor()).toEqual(expect.objectContaining({ id: 123 }));
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
