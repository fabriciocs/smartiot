import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../configuracao-alerta.test-samples';

import { ConfiguracaoAlertaFormService } from './configuracao-alerta-form.service';

describe('ConfiguracaoAlerta Form Service', () => {
  let service: ConfiguracaoAlertaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConfiguracaoAlertaFormService);
  });

  describe('Service methods', () => {
    describe('createConfiguracaoAlertaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createConfiguracaoAlertaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            limite: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });

      it('passing IConfiguracaoAlerta should create a new form with FormGroup', () => {
        const formGroup = service.createConfiguracaoAlertaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            limite: expect.any(Object),
            email: expect.any(Object),
          }),
        );
      });
    });

    describe('getConfiguracaoAlerta', () => {
      it('should return NewConfiguracaoAlerta for default ConfiguracaoAlerta initial value', () => {
        const formGroup = service.createConfiguracaoAlertaFormGroup(sampleWithNewData);

        const configuracaoAlerta = service.getConfiguracaoAlerta(formGroup) as any;

        expect(configuracaoAlerta).toMatchObject(sampleWithNewData);
      });

      it('should return NewConfiguracaoAlerta for empty ConfiguracaoAlerta initial value', () => {
        const formGroup = service.createConfiguracaoAlertaFormGroup();

        const configuracaoAlerta = service.getConfiguracaoAlerta(formGroup) as any;

        expect(configuracaoAlerta).toMatchObject({});
      });

      it('should return IConfiguracaoAlerta', () => {
        const formGroup = service.createConfiguracaoAlertaFormGroup(sampleWithRequiredData);

        const configuracaoAlerta = service.getConfiguracaoAlerta(formGroup) as any;

        expect(configuracaoAlerta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IConfiguracaoAlerta should not enable id FormControl', () => {
        const formGroup = service.createConfiguracaoAlertaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewConfiguracaoAlerta should disable id FormControl', () => {
        const formGroup = service.createConfiguracaoAlertaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
