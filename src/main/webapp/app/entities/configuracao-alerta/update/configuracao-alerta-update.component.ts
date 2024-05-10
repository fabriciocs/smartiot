import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IConfiguracaoAlerta } from '../configuracao-alerta.model';
import { ConfiguracaoAlertaService } from '../service/configuracao-alerta.service';
import { ConfiguracaoAlertaFormService, ConfiguracaoAlertaFormGroup } from './configuracao-alerta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-configuracao-alerta-update',
  templateUrl: './configuracao-alerta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConfiguracaoAlertaUpdateComponent implements OnInit {
  isSaving = false;
  configuracaoAlerta: IConfiguracaoAlerta | null = null;

  protected configuracaoAlertaService = inject(ConfiguracaoAlertaService);
  protected configuracaoAlertaFormService = inject(ConfiguracaoAlertaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConfiguracaoAlertaFormGroup = this.configuracaoAlertaFormService.createConfiguracaoAlertaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configuracaoAlerta }) => {
      this.configuracaoAlerta = configuracaoAlerta;
      if (configuracaoAlerta) {
        this.updateForm(configuracaoAlerta);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configuracaoAlerta = this.configuracaoAlertaFormService.getConfiguracaoAlerta(this.editForm);
    if (configuracaoAlerta.id !== null) {
      this.subscribeToSaveResponse(this.configuracaoAlertaService.update(configuracaoAlerta));
    } else {
      this.subscribeToSaveResponse(this.configuracaoAlertaService.create(configuracaoAlerta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfiguracaoAlerta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(configuracaoAlerta: IConfiguracaoAlerta): void {
    this.configuracaoAlerta = configuracaoAlerta;
    this.configuracaoAlertaFormService.resetForm(this.editForm, configuracaoAlerta);
  }
}
