import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IConfiguracaoAlerta } from '../configuracao-alerta.model';

@Component({
  standalone: true,
  selector: 'jhi-configuracao-alerta-detail',
  templateUrl: './configuracao-alerta-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConfiguracaoAlertaDetailComponent {
  configuracaoAlerta = input<IConfiguracaoAlerta | null>(null);

  previousState(): void {
    window.history.back();
  }
}
