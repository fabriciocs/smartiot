import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITransmitter } from '../transmitter.model';
import { TransmitterService } from '../service/transmitter.service';

@Component({
  standalone: true,
  templateUrl: './transmitter-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TransmitterDeleteDialogComponent {
  transmitter?: ITransmitter;

  protected transmitterService = inject(TransmitterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transmitterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
