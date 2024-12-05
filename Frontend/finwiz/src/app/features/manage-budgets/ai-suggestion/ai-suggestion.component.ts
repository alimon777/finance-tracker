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
  isAiBudgetGenerated: boolean = false;

  // private suggestionCount: number = 0;
  // private lastResetTime: number = 0;
  // private SUGGESTION_LIMIT: number = 3;
  // private RATE_LIMIT_DURATION: number = 60000;

  constructor(
    private budgetService: BudgetService,
    private storageService: StorageService,
    private snackbarService: SnackbarService,
  ) { }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
  }

  refreshSuggestion(): void {
    // const currentTime = Date.now();

    // // Reset count if more than a minute has passed
    // if (currentTime - this.lastResetTime > this.RATE_LIMIT_DURATION) {
    //   this.suggestionCount = 0;
    //   this.lastResetTime = currentTime;
    // }

    // // Check if suggestion limit is exceeded
    // if (this.suggestionCount >= this.SUGGESTION_LIMIT) {
    //   const waitTime = Math.ceil((this.RATE_LIMIT_DURATION - (currentTime - this.lastResetTime)) / 1000);
    //   this.snackbarService.show(`Please wait ${waitTime} seconds before generating another suggestion`);
    //   return;
    // }

    this.isAiBudgetGenerated = true;
    this.generateSuggestion();
    // this.suggestionCount++;
  }

  generateSuggestion(): void {
    this.isAiBudgetGenerated = true;
    this.budgetService.getAiSuggestion().subscribe({
      next: (data: AiSuggestion) => {
        // this.storageService.storeAiSuggestion(data);
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
    this.budgetAdded.emit(this.budget);
  }
}
