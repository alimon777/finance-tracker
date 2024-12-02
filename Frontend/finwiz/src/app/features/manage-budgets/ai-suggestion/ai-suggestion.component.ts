import { Component, EventEmitter, Output } from '@angular/core';
import { Budget } from 'src/app/shared/models/budget';

@Component({
  selector: 'app-ai-suggestion',
  templateUrl: './ai-suggestion.component.html',
  styleUrls: ['./ai-suggestion.component.css']
})
export class AiSuggestionComponent {
  @Output() budgetAdded = new EventEmitter<Budget>();

  budget: Budget = {
    id: 0,
    userId: 0,
    budgetStartDate: new Date(2024, 11, 1),
    budgetEndDate: new Date(2025, 0, 1),
    food: 10000,
    housing: 15000,
    transportation: 5000,
    entertainment: 3000,
    aiGenerated: true,
    total: 33000,
  }

  isAiBudgetGenerated: boolean = false;
  aiSuggestion: string = "Based on your financial data, our AI recommends optimizing your budget by reducing entertainment expenses and allocating more towards savings and investments. Consider exploring cost-effective alternatives and setting up automatic savings.";

  refreshSuggestion(): void {
    this.isAiBudgetGenerated = true;
  }

  generateSuggestion(): void {
    this.isAiBudgetGenerated = false;
  }

  onSubmitBudget() {
    this.budgetAdded.emit(this.budget);
  }
}
