import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMeasurement } from '../measurement.model';

@Component({
  standalone: true,
  selector: 'jhi-measurement-detail',
  templateUrl: './measurement-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MeasurementDetailComponent {
  measurement = input<IMeasurement | null>(null);

  previousState(): void {
    window.history.back();
  }
}
