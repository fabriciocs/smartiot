import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMeasurement } from '../measurement.model';
import { MeasurementService } from '../service/measurement.service';

@Component({
  standalone: true,
  templateUrl: './measurement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MeasurementDeleteDialogComponent {
  measurement?: IMeasurement;

  protected measurementService = inject(MeasurementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.measurementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
