import { Component, OnInit } from '@angular/core';
import { BudgetService } from './../budget.service';
import { Budget } from 'src/app/features/manage-budgets/budget.model';
import { StorageService } from 'src/app/core/services/storage/storage.service'
import { SnackbarService } from 'src/app/core/services/snackbar/snackbar.service';

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html',
  styleUrls: ['./budget.component.css']
})

export class BudgetComponent implements OnInit {

  budgets: Budget[] = [];
  userId: number = 0;
  isAddBudgetVisible: boolean = false;

  constructor(
    private budgetService: BudgetService,
    private storageService: StorageService,
    private snackbarService: SnackbarService,
  ) { }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
    if (this.userId) {
      this.loadBudgets();
    } else {
      console.error('User ID not found in local storage');
    }
  }

  saveBudget(budgetData: Budget) {
    console.log(budgetData);
    budgetData.userId = this.userId;
    this.budgetService.createBudget(budgetData).subscribe({
      next: (createdBudget) => {
        this.budgets.push(createdBudget);
        this.snackbarService.show("Budget Added Successfully");
      },
      error: (error) => {
        this.snackbarService.show(error.message);
      }
    });
  }


  loadBudgets(): void {
    this.budgetService.getBudgetsByUserId().subscribe(
      data => {
        this.budgets = data;
      },
      error => {
        this.snackbarService.show(error.message);
      }
    );
  }

  deleteBudget(id: number): void {
    this.budgetService.deleteBudget(id).subscribe(
      () => {
        this.loadBudgets();
        this.snackbarService.show('Budget deleted successfully');
      },
      error => {
        this.snackbarService.show(error.message);
      }
    );
  }

  openAddBudgetModal(): void {
    this.isAddBudgetVisible = true;
  }

  onAddBudgetModalClose(): void {
    this.isAddBudgetVisible = false;
  }
}
