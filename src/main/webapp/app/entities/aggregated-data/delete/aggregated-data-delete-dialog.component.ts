import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAggregatedData } from '../aggregated-data.model';
import { AggregatedDataService } from '../service/aggregated-data.service';

@Component({
  standalone: true,
  templateUrl: './aggregated-data-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AggregatedDataDeleteDialogComponent {
  aggregatedData?: IAggregatedData;

  protected aggregatedDataService = inject(AggregatedDataService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aggregatedDataService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
