import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConfiguracaoAlerta } from '../configuracao-alerta.model';
import { ConfiguracaoAlertaService } from '../service/configuracao-alerta.service';

@Component({
  standalone: true,
  templateUrl: './configuracao-alerta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConfiguracaoAlertaDeleteDialogComponent {
  configuracaoAlerta?: IConfiguracaoAlerta;

  protected configuracaoAlertaService = inject(ConfiguracaoAlertaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configuracaoAlertaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
