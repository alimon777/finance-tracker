import { Component, OnInit } from '@angular/core';
import { BudgetService } from 'src/app/service/budget/budget.service';
import { Budget } from 'src/app/models/budget';

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html',
  styleUrls: ['./budget.component.css']
})
export class BudgetComponent implements OnInit {
  budgets: Budget[] = [];
  budget: Budget = new Budget();
  userId: number = 0; // Remove initial value

  constructor(private budgetService: BudgetService) {}

  ngOnInit(): void {
    // Retrieve userId from local storage
    const storedUserId = localStorage.getItem('userId');
    this.userId = storedUserId ? +storedUserId : 0; // Convert to number or set default to 0

    if (this.userId) {
      this.loadBudgets();
    } else {
      console.error('User ID not found in local storage');
      // Handle error or redirect if needed
    }
  }

  loadBudgets(): void {
    this.budgetService.getBudgetsByUserId(this.userId).subscribe(data => {
      this.budgets = data;
    });
  }

  saveBudget(): void {
    // Check if the start date is greater than the end date
    if (new Date(this.budget.budgetStartDate) > new Date(this.budget.budgetEndDate)) {
      alert('Start Date cannot be later than End Date.'); // Display an alert or use a different notification method
      return; // Exit the function if validation fails
    }

    this.budget.userId = this.userId; // Set userId from local storage
    this.budgetService.createBudget(this.budget).subscribe(() => {
      this.loadBudgets();
      this.budget = new Budget(); // Reset form
    });
  }

  deleteBudget(id: number): void {
    this.budgetService.deleteBudget(this.userId, id).subscribe(() => {
      this.loadBudgets();
    });
  }
}
