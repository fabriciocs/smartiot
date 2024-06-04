import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RepeaterDetailComponent } from './repeater-detail.component';

describe('Repeater Management Detail Component', () => {
  let comp: RepeaterDetailComponent;
  let fixture: ComponentFixture<RepeaterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepeaterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RepeaterDetailComponent,
              resolve: { repeater: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RepeaterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RepeaterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load repeater on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RepeaterDetailComponent);

      // THEN
      expect(instance.repeater()).toEqual(expect.objectContaining({ id: 123 }));
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
