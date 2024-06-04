import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TransmitterDetailComponent } from './transmitter-detail.component';

describe('Transmitter Management Detail Component', () => {
  let comp: TransmitterDetailComponent;
  let fixture: ComponentFixture<TransmitterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransmitterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TransmitterDetailComponent,
              resolve: { transmitter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TransmitterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TransmitterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transmitter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TransmitterDetailComponent);

      // THEN
      expect(instance.transmitter()).toEqual(expect.objectContaining({ id: 123 }));
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
