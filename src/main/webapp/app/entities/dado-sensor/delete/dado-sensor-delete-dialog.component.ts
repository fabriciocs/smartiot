import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDadoSensor } from '../dado-sensor.model';
import { DadoSensorService } from '../service/dado-sensor.service';

@Component({
  standalone: true,
  templateUrl: './dado-sensor-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DadoSensorDeleteDialogComponent {
  dadoSensor?: IDadoSensor;

  protected dadoSensorService = inject(DadoSensorService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dadoSensorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
