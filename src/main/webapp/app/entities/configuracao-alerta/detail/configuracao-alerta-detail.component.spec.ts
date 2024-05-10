import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConfiguracaoAlertaDetailComponent } from './configuracao-alerta-detail.component';

describe('ConfiguracaoAlerta Management Detail Component', () => {
  let comp: ConfiguracaoAlertaDetailComponent;
  let fixture: ComponentFixture<ConfiguracaoAlertaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfiguracaoAlertaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConfiguracaoAlertaDetailComponent,
              resolve: { configuracaoAlerta: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConfiguracaoAlertaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfiguracaoAlertaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load configuracaoAlerta on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConfiguracaoAlertaDetailComponent);

      // THEN
      expect(instance.configuracaoAlerta()).toEqual(expect.objectContaining({ id: 123 }));
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
