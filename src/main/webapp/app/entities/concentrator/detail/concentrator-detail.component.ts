import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IConcentrator } from '../concentrator.model';

@Component({
  standalone: true,
  selector: 'jhi-concentrator-detail',
  templateUrl: './concentrator-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConcentratorDetailComponent {
  concentrator = input<IConcentrator | null>(null);

  previousState(): void {
    window.history.back();
  }
}
