import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISysUser } from '../sys-user.model';
import { SysUserService } from '../service/sys-user.service';
import { SysUserFormService, SysUserFormGroup } from './sys-user-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sys-user-update',
  templateUrl: './sys-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SysUserUpdateComponent implements OnInit {
  isSaving = false;
  sysUser: ISysUser | null = null;

  protected sysUserService = inject(SysUserService);
  protected sysUserFormService = inject(SysUserFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SysUserFormGroup = this.sysUserFormService.createSysUserFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sysUser }) => {
      this.sysUser = sysUser;
      if (sysUser) {
        this.updateForm(sysUser);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sysUser = this.sysUserFormService.getSysUser(this.editForm);
    if (sysUser.id !== null) {
      this.subscribeToSaveResponse(this.sysUserService.update(sysUser));
    } else {
      this.subscribeToSaveResponse(this.sysUserService.create(sysUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISysUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sysUser: ISysUser): void {
    this.sysUser = sysUser;
    this.sysUserFormService.resetForm(this.editForm, sysUser);
  }
}
