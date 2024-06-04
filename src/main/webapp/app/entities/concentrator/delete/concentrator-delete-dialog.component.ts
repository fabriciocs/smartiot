import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConcentrator } from '../concentrator.model';
import { ConcentratorService } from '../service/concentrator.service';

@Component({
  standalone: true,
  templateUrl: './concentrator-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConcentratorDeleteDialogComponent {
  concentrator?: IConcentrator;

  protected concentratorService = inject(ConcentratorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.concentratorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
