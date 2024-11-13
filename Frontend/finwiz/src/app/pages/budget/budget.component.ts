import { Component, OnInit } from '@angular/core';
import { BudgetService } from 'src/app/service/budget/budget.service';
import { Budget } from 'src/app/models/budget';
import { StorageService } from 'src/app/service/storage/storage.service';
import { UserDetails } from 'src/app/models/user-details';

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html',
  styleUrls: ['./budget.component.css']
})
export class BudgetComponent implements OnInit {
  budgets: Budget[] = [];
  budget: Budget = new Budget();
  userId: number = 0;
  userDetails : UserDetails = { username:"", email:""}

  constructor(
    private budgetService: BudgetService,
    private storageService: StorageService
  ) {}

  currentDate: string = new Date().toISOString().split('T')[0];

  total:number = 0;
  updateTotal() {
    this.total = (this.budget.food || 0) + (this.budget.housing || 0) + 
                 (this.budget.transportation || 0) + (this.budget.entertainment || 0);
  }
  ngOnInit(): void {    
    this.userId = this.storageService.fetchUserId();
    this.userDetails = this.storageService.fetchUserDetails();
    if (this.userId) {
      this.loadBudgets();
    } else {
      console.error('User ID not found in local storage');

    }
  }

  loadBudgets(): void {
    this.budgetService.getBudgetsByUserId(this.userId).subscribe(data => {
      this.budgets = data;
    });
  }

  saveBudget(): void {
    this.budget.userId = this.userId;
    this.budget.aiGenerated = false;
    this.budget.total=this.total;
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
