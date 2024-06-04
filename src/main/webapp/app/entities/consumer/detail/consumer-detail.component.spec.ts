import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConsumerDetailComponent } from './consumer-detail.component';

describe('Consumer Management Detail Component', () => {
  let comp: ConsumerDetailComponent;
  let fixture: ComponentFixture<ConsumerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsumerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConsumerDetailComponent,
              resolve: { consumer: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConsumerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsumerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load consumer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConsumerDetailComponent);

      // THEN
      expect(instance.consumer()).toEqual(expect.objectContaining({ id: 123 }));
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
