import { Component, OnInit } from '@angular/core';
import { BudgetService } from 'src/app/shared/services/budget/budget.service';
import { Budget } from 'src/app/shared/models/budget';
import { UserDetails } from 'src/app/shared/models/user-details';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StorageService } from 'src/app/core/services/storage/storage.service'
import { SnackbarService } from 'src/app/core/services/snackbar/snackbar.service';

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html',
  styleUrls: ['./budget.component.css']
})
export class BudgetComponent implements OnInit {
  budgets: Budget[] = [];
  budgetForm!: FormGroup;
  userId: number = 0;
  userDetails: UserDetails = { username: "", email: "" };
  currentDate: string = new Date().toISOString().split('T')[0];
  total: number = 0;

  constructor(
    private fb: FormBuilder,
    private budgetService: BudgetService,
    private storageService: StorageService,
    private snackbarService: SnackbarService,
  ) { }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
    this.userDetails = this.storageService.fetchUserDetails();
    if (this.userId) {
      this.loadBudgets();
      this.initializeForm();
    } else {
      console.error('User ID not found in local storage');
    }
  }

  initializeForm(): void {
    this.budgetForm = this.fb.group({
      budgetStartDate: [null, Validators.required],
      budgetEndDate: [null, Validators.required],
      food: [null, [Validators.required, Validators.min(0)]],
      housing: [null, [Validators.required, Validators.min(0)]],
      transportation: [null, [Validators.required, Validators.min(0)]],
      entertainment: [null, [Validators.required, Validators.min(0)]]
    });

    this.budgetForm.get('budgetStartDate')?.valueChanges.subscribe(() => {
      this.budgetForm.get('budgetEndDate')?.reset();
    });
    // Subscribe to form value changes to calculate total dynamically
    this.budgetForm.valueChanges.subscribe(() => {
      this.updateTotal();
    });
  }

  // Calculate total budget dynamically
  updateTotal(): void {
    const formValue = this.budgetForm.value;
    this.total = (formValue.food || 0) + (formValue.housing || 0) +
      (formValue.transportation || 0) + (formValue.entertainment || 0);
  }

  loadBudgets(): void {
    this.budgetService.getBudgetsByUserId(this.userId).subscribe(
      data => {
        this.budgets = data;
      },
      error => {
        this.snackbarService.show(error.message);
      }
    );
  }

  // Save budget after form validation
  saveBudget(): void {
    if (this.budgetForm.invalid) {
      return;
    }

    const budgetData = this.budgetForm.value;
    budgetData.userId = this.userId;
    budgetData.aiGenerated = false;
    budgetData.total = this.total;

    this.budgetService.createBudget(budgetData).subscribe(
      () => {
        this.loadBudgets();
        this.budgetForm.reset();
        this.snackbarService.show('Budget created successfully');
      },
      error => {
        this.snackbarService.show(error.message);
      }
    );
  }

  deleteBudget(id: number): void {
    this.budgetService.deleteBudget(this.userId, id).subscribe(
      () => {
        this.loadBudgets();
        this.snackbarService.show('Budget deleted successfully');
      },
      error => {
        this.snackbarService.show(error.message);
      }
    );
  }

  get formControls() {
    return this.budgetForm.controls;
  }
}
