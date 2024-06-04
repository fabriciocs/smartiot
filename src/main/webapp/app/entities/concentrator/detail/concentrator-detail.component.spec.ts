import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConcentratorDetailComponent } from './concentrator-detail.component';

describe('Concentrator Management Detail Component', () => {
  let comp: ConcentratorDetailComponent;
  let fixture: ComponentFixture<ConcentratorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConcentratorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConcentratorDetailComponent,
              resolve: { concentrator: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConcentratorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConcentratorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load concentrator on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConcentratorDetailComponent);

      // THEN
      expect(instance.concentrator()).toEqual(expect.objectContaining({ id: 123 }));
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
