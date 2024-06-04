import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPricing } from '../pricing.model';
import { PricingService } from '../service/pricing.service';

@Component({
  standalone: true,
  templateUrl: './pricing-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PricingDeleteDialogComponent {
  pricing?: IPricing;

  protected pricingService = inject(PricingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pricingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
