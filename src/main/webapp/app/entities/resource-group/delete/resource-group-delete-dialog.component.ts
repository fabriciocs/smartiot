import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IResourceGroup } from '../resource-group.model';
import { ResourceGroupService } from '../service/resource-group.service';

@Component({
  standalone: true,
  templateUrl: './resource-group-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResourceGroupDeleteDialogComponent {
  resourceGroup?: IResourceGroup;

  protected resourceGroupService = inject(ResourceGroupService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resourceGroupService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
