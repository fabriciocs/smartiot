import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IConsumer } from '../consumer.model';
import { ConsumerService } from '../service/consumer.service';

@Component({
  standalone: true,
  templateUrl: './consumer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConsumerDeleteDialogComponent {
  consumer?: IConsumer;

  protected consumerService = inject(ConsumerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consumerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
