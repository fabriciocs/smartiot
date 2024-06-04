import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IManualEntry } from '../manual-entry.model';
import { ManualEntryService } from '../service/manual-entry.service';

@Component({
  standalone: true,
  templateUrl: './manual-entry-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ManualEntryDeleteDialogComponent {
  manualEntry?: IManualEntry;

  protected manualEntryService = inject(ManualEntryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manualEntryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
