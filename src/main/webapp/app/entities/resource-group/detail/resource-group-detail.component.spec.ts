import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ResourceGroupDetailComponent } from './resource-group-detail.component';

describe('ResourceGroup Management Detail Component', () => {
  let comp: ResourceGroupDetailComponent;
  let fixture: ComponentFixture<ResourceGroupDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResourceGroupDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ResourceGroupDetailComponent,
              resolve: { resourceGroup: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ResourceGroupDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourceGroupDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resourceGroup on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ResourceGroupDetailComponent);

      // THEN
      expect(instance.resourceGroup()).toEqual(expect.objectContaining({ id: 123 }));
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
