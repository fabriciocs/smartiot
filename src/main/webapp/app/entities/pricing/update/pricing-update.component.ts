import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPricing } from '../pricing.model';
import { PricingService } from '../service/pricing.service';
import { PricingFormService, PricingFormGroup } from './pricing-form.service';

@Component({
  standalone: true,
  selector: 'jhi-pricing-update',
  templateUrl: './pricing-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PricingUpdateComponent implements OnInit {
  isSaving = false;
  pricing: IPricing | null = null;

  protected pricingService = inject(PricingService);
  protected pricingFormService = inject(PricingFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PricingFormGroup = this.pricingFormService.createPricingFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pricing }) => {
      this.pricing = pricing;
      if (pricing) {
        this.updateForm(pricing);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pricing = this.pricingFormService.getPricing(this.editForm);
    if (pricing.id !== null) {
      this.subscribeToSaveResponse(this.pricingService.update(pricing));
    } else {
      this.subscribeToSaveResponse(this.pricingService.create(pricing));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPricing>>): void {
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

  protected updateForm(pricing: IPricing): void {
    this.pricing = pricing;
    this.pricingFormService.resetForm(this.editForm, pricing);
  }
}
