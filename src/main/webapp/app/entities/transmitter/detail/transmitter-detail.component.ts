import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITransmitter } from '../transmitter.model';

@Component({
  standalone: true,
  selector: 'jhi-transmitter-detail',
  templateUrl: './transmitter-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TransmitterDetailComponent {
  transmitter = input<ITransmitter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
