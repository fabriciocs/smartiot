import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRepeater } from '../repeater.model';

@Component({
  standalone: true,
  selector: 'jhi-repeater-detail',
  templateUrl: './repeater-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RepeaterDetailComponent {
  repeater = input<IRepeater | null>(null);

  previousState(): void {
    window.history.back();
  }
}
