import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISysUser } from '../sys-user.model';

@Component({
  standalone: true,
  selector: 'jhi-sys-user-detail',
  templateUrl: './sys-user-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SysUserDetailComponent {
  sysUser = input<ISysUser | null>(null);

  previousState(): void {
    window.history.back();
  }
}
