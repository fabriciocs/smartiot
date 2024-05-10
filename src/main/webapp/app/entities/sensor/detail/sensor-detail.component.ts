import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISensor } from '../sensor.model';

@Component({
  standalone: true,
  selector: 'jhi-sensor-detail',
  templateUrl: './sensor-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SensorDetailComponent {
  sensor = input<ISensor | null>(null);

  previousState(): void {
    window.history.back();
  }
}
