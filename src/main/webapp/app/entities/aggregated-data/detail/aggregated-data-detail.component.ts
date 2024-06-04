import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAggregatedData } from '../aggregated-data.model';

@Component({
  standalone: true,
  selector: 'jhi-aggregated-data-detail',
  templateUrl: './aggregated-data-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AggregatedDataDetailComponent {
  aggregatedData = input<IAggregatedData | null>(null);

  previousState(): void {
    window.history.back();
  }
}
