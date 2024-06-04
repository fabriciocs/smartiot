import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISysUser } from '../sys-user.model';
import { SysUserService } from '../service/sys-user.service';

@Component({
  standalone: true,
  templateUrl: './sys-user-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SysUserDeleteDialogComponent {
  sysUser?: ISysUser;

  protected sysUserService = inject(SysUserService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sysUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
