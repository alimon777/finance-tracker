import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Budget } from 'src/app/models/budget';
import { Transaction } from 'src/app/models/transaction';
import { AiService } from 'src/app/service/ai/ai.service';
import { BudgetService } from 'src/app/service/budget/budget.service';
import { TransactionService } from 'src/app/service/transaction/transaction.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ai',
  templateUrl: './ai.component.html',
  styleUrls: ['./ai.component.css']
})
export class AiComponent implements OnInit {
  constructor(
    private suggestionService: AiService,
    private transactionService: TransactionService, 
    private budgetService: BudgetService,
    private router: Router,
  ) {}

  transactions: Transaction[] = [];
  budgetSuggestion: string = '';  // Store the AI-generated budget suggestion
  loading: boolean = false;
  errorMessage: string = '';
  userId: number = 0;
  aiBudget: Budget = new Budget();
  suggestion: string = '';

  ngOnInit(): void {
    this.loadUserId();
    this.loadTransactions();
  }

  loadUserId(): void {
    const storedUserId = localStorage.getItem('userId');
    this.userId = storedUserId ? +storedUserId : 0; 
  }

  loadTransactions(): void {
    if (this.userId) {
      this.transactionService.getTransactions(this.userId).subscribe({
        next: (response: Transaction[]) => {
          this.transactions = response;
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading transactions:', error.message);
        },
      });
    } else {
      console.warn('User ID not found in local storage.');
    }
  }

  async onGetBudgetSuggestion(): Promise<void> {
    this.loading = true;
    this.errorMessage = '';
    this.budgetSuggestion = '';

    try {
      await this.loadTransactions();
      this.suggestion = await this.suggestionService.generateBudgetSuggestion(this.transactions);
      this.budgetSuggestion = this.formatResponse(this.suggestion);
    } catch (error) {
      this.errorMessage = 'An error occurred while generating the budget suggestion.';
    } finally {
      this.loading = false;
    }
  }

  onAddToBudget(): void {
    this.aiBudget = this.suggestionService.parseAiGeneratedBudgetResponse(this.suggestion, this.userId);
    console.log('Budget suggestion added:', this.aiBudget);
  
    this.budgetService.createBudget(this.aiBudget).subscribe(() => {
      const confirmMessage = 'The budget has been successfully added! Do you want to go to the Budget page?';
      if (window.confirm(confirmMessage)) {
        this.router.navigate(['/budget']);
      } else {
        alert('Budget added successfully!');
      }
    });
  }
  
  formatResponse(response: string): string {
    response = response.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
    response = response.replace(/\*(.*?)\*/g, '<em>$1</em>');
    response = response.replace(/\n/g, '<br/>');
    response = response.replace(/^(\d+)\. (.*)/gm, '<ol><li>$2</li></ol>');
    response = response.replace(/^\* (.*)/gm, '<ul><li>$1</li></ul>');
    return response;
  }
}


