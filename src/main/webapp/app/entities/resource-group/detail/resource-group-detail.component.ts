import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IResourceGroup } from '../resource-group.model';

@Component({
  standalone: true,
  selector: 'jhi-resource-group-detail',
  templateUrl: './resource-group-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ResourceGroupDetailComponent {
  resourceGroup = input<IResourceGroup | null>(null);

  previousState(): void {
    window.history.back();
  }
}
