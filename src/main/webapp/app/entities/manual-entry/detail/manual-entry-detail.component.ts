import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IManualEntry } from '../manual-entry.model';

@Component({
  standalone: true,
  selector: 'jhi-manual-entry-detail',
  templateUrl: './manual-entry-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ManualEntryDetailComponent {
  manualEntry = input<IManualEntry | null>(null);

  previousState(): void {
    window.history.back();
  }
}
