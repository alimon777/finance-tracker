import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StorageService } from 'src/app/core/services/storage/storage.service';
import { BudgetService } from 'src/app/shared/services/budget/budget.service';

@Component({
  selector: 'app-add-budget',
  templateUrl: './add-budget.component.html',
  styleUrls: ['./add-budget.component.css']
})
export class AddBudgetComponent implements OnInit {
  @Output() close = new EventEmitter<void>();
  @Output() budgetAdded = new EventEmitter<void>();

  budgetForm!: FormGroup;
  isVisible: boolean = true;
  userId: number = 0;
  errorMessage: string = '';
  currentDate: string = new Date().toISOString().split('T')[0];
  total: number = 0;

  constructor(
    private fb: FormBuilder,
    private budgetService: BudgetService,
    private storageService: StorageService
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();

    // Clear error message when form values change
    this.budgetForm.valueChanges.subscribe(() => {
      this.errorMessage = '';
    });
  }

  initializeForm(): void {
    this.budgetForm = this.fb.group({
      budgetStartDate: [null, [
        Validators.required
      ]],
      budgetEndDate: [null, [
        Validators.required
      ]],
      food: [null, [
        Validators.required, 
        Validators.min(0)
      ]],
      housing: [null, [
        Validators.required, 
        Validators.min(0)
      ]],
      transportation: [null, [
        Validators.required, 
        Validators.min(0)
      ]],
      entertainment: [null, [
        Validators.required, 
        Validators.min(0)
      ]]
    }, { validators: this.dateRangeValidator });

    // Reset end date when start date changes
    this.budgetForm.get('budgetStartDate')?.valueChanges.subscribe(() => {
      this.budgetForm.get('budgetEndDate')?.reset();
    });

    // Calculate total on form value changes
    this.budgetForm.valueChanges.subscribe(() => {
      this.updateTotal();
    });
  }

  // Custom validator to ensure end date is after start date
  dateRangeValidator(form: FormGroup) {
    const startDate = form.get('budgetStartDate')?.value;
    const endDate = form.get('budgetEndDate')?.value;

    return startDate && endDate && new Date(endDate) >= new Date(startDate) 
      ? null 
      : { dateRange: true };
  }

  // Save budget method
  onSubmitBudget(): void {
    if (this.budgetForm.invalid) {
      // Mark all fields as touched to trigger validation display
      Object.keys(this.budgetForm.controls).forEach(key => {
        const control = this.budgetForm.get(key);
        control?.markAsTouched();
      });
      return;
    }

    const budgetData = {
      ...this.budgetForm.value,
      userId: this.userId,
      aiGenerated: false,
      total: this.total
    };

    this.budgetService.createBudget(budgetData).subscribe(
      () => {
        this.budgetAdded.emit();
        this.onCancel();
      },
      error => {
        this.errorMessage = error.message || 'An error occurred while saving the budget.';
      }
    );
  }

  // Calculate total budget dynamically
  updateTotal(): void {
    const formValue = this.budgetForm.value;
    this.total = (
      (formValue.food || 0) + 
      (formValue.housing || 0) + 
      (formValue.transportation || 0) + 
      (formValue.entertainment || 0)
    );
  }

  // Cancel method to close the modal
  onCancel(): void {
    this.isVisible = false;
    this.close.emit();
  }

  // Getter methods for easy access in template
  get budgetStartDate() { return this.budgetForm.get('budgetStartDate'); }
  get budgetEndDate() { return this.budgetForm.get('budgetEndDate'); }
  get food() { return this.budgetForm.get('food'); }
  get housing() { return this.budgetForm.get('housing'); }
  get transportation() { return this.budgetForm.get('transportation'); }
  get entertainment() { return this.budgetForm.get('entertainment'); }
}