import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MeterDetailComponent } from './meter-detail.component';

describe('Meter Management Detail Component', () => {
  let comp: MeterDetailComponent;
  let fixture: ComponentFixture<MeterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MeterDetailComponent,
              resolve: { meter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MeterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MeterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load meter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MeterDetailComponent);

      // THEN
      expect(instance.meter()).toEqual(expect.objectContaining({ id: 123 }));
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
