import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMeter } from '../meter.model';

@Component({
  standalone: true,
  selector: 'jhi-meter-detail',
  templateUrl: './meter-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MeterDetailComponent {
  meter = input<IMeter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
