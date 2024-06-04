import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IConsumer } from '../consumer.model';

@Component({
  standalone: true,
  selector: 'jhi-consumer-detail',
  templateUrl: './consumer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConsumerDetailComponent {
  consumer = input<IConsumer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
