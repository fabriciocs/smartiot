import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPricing } from '../pricing.model';

@Component({
  standalone: true,
  selector: 'jhi-pricing-detail',
  templateUrl: './pricing-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PricingDetailComponent {
  pricing = input<IPricing | null>(null);

  previousState(): void {
    window.history.back();
  }
}
