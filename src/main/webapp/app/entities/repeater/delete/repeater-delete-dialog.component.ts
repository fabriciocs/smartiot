import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRepeater } from '../repeater.model';
import { RepeaterService } from '../service/repeater.service';

@Component({
  standalone: true,
  templateUrl: './repeater-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RepeaterDeleteDialogComponent {
  repeater?: IRepeater;

  protected repeaterService = inject(RepeaterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.repeaterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
