import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICostCenter } from '../cost-center.model';

@Component({
  standalone: true,
  selector: 'jhi-cost-center-detail',
  templateUrl: './cost-center-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CostCenterDetailComponent {
  costCenter = input<ICostCenter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
