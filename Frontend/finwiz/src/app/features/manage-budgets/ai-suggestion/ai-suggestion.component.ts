import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AiSuggestion, Budget } from 'src/app/features/manage-budgets/budget.model';
import { BudgetService } from './../budget.service';
import { StorageService } from 'src/app/core/services/storage/storage.service';
import { SnackbarService } from 'src/app/core/services/snackbar/snackbar.service';

@Component({
  selector: 'app-ai-suggestion',
  templateUrl: './ai-suggestion.component.html',
  styleUrls: ['./ai-suggestion.component.css']
})
export class AiSuggestionComponent implements OnInit {

  @Output() budgetAdded = new EventEmitter<Budget>();

  suggestionText: string = '';
  budget: Budget = {
    id: 0,
    userId: 0,
    budgetStartDate: new Date(),
    budgetEndDate: new Date(),
    food: 0,
    housing: 0,
    transportation: 0,
    entertainment: 0,
    aiGenerated: true,
    total: 0,
  }
  userId: number = 0;
  isBudgetAdded: boolean = true;
  isAiBudgetGenerated: boolean = false;

  constructor(
    private budgetService: BudgetService,
    private storageService: StorageService,
    private snackbarService: SnackbarService,
  ) { }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
  }

  refreshSuggestion(): void {
    this.isAiBudgetGenerated = true;
    this.generateSuggestion();
  }

  generateSuggestion(): void {
    this.isAiBudgetGenerated = true;
    this.isBudgetAdded = false;
    this.budgetService.getAiSuggestion().subscribe({
      next: (data: AiSuggestion) => {
        this.suggestionText = data.textContent;
        this.budget = data.budget;
        this.budget.total = this.calculateTotal(this.budget);
      },
      error: (error) => {
        this.snackbarService.show('Error fetching suggestion', error);
      }
    });
  }

  calculateTotal(budget: Budget): number {
    return budget.food + budget.housing + budget.entertainment + budget.transportation;
  }

  onSubmitBudget() {
    this.isBudgetAdded = true;
    this.budgetAdded.emit(this.budget);
  }
}
