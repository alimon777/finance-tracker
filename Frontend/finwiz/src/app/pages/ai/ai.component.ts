import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Budget } from 'src/app/models/budget';
import { Transaction } from 'src/app/models/transaction';
import { AiService } from 'src/app/service/ai/ai.service';
import { BudgetService } from 'src/app/service/budget/budget.service';
import { TransactionService } from 'src/app/service/transaction/transaction.service';
import { Router } from '@angular/router';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-ai',
  templateUrl: './ai.component.html',
  styleUrls: ['./ai.component.css'],
  animations: [
    trigger('slideIn', [
      transition(':enter', [
        style({ transform: 'translateY(100%)', opacity: 0 }),  // Content starts slightly down and invisible
        animate('0.5ms', style({ transform: 'translateY(0)', opacity: 1 }))  // Content slides up
      ]),
      transition(':leave', [
        animate('0.5ms', style({ transform: 'translateY(100%)', opacity: 0 }))  // Fade out while sliding down
      ])
    ]),

    // Slide up animation for the background
    trigger('slideUp', [
      transition(':enter', [
        style({ backgroundPosition: 'center 20%' }),  // Start with background position 20% down
        animate('0.8s ease-out', style({ backgroundPosition: 'center 50%' }))  // Slide up to its final position
      ])
    ]),
    trigger('moveUp', [
      transition(':enter', [
        style({ transform: 'translateY(100%)' }),  // Start with content hidden below
        animate('750ms ease-out', style({ transform: 'translateY(0)' }))  // Slide up to position
      ])
    ])
  ]
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
    this.loadBudgetSuggestion();  // Load previously stored budget suggestion
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

  // Load saved budget suggestion from localStorage
  loadBudgetSuggestion(): void {
    
    const savedSuggestion = localStorage.getItem('budgetSuggestion');
    if (savedSuggestion) {
      this.budgetSuggestion = savedSuggestion;
    }
  }

  async onGetBudgetSuggestion(): Promise<void> {
    this.loading = true;
    this.errorMessage = '';
    this.budgetSuggestion = '';

    if (this.transactions.length === 0) {
      this.loading = false;
      // Show the alert
      window.alert('No transactions found. Please add transactions to generate a budget suggestion.');
      return;
    }
  
    try {
      await this.loadTransactions();
      this.suggestion = await this.suggestionService.generateBudgetSuggestion(this.transactions);
      this.budgetSuggestion = this.formatResponse(this.suggestion);
      
      // Save the new budget suggestion to localStorage
      localStorage.setItem('budgetSuggestion', this.budgetSuggestion);
    } catch (error) {
      this.errorMessage = 'An error occurred while generating the budget suggestion.';
    } finally {
      this.loading = false;
    }
  }

  async onRegenerateBudgetSuggestion(): Promise<void> {
    this.loading = true;
    this.errorMessage = '';
    
    try {
      const regeneratedSuggestion = await this.suggestionService.generateBudgetSuggestion(this.transactions);
      this.budgetSuggestion = this.formatResponse(regeneratedSuggestion);
      
      // Save the regenerated suggestion to localStorage
      localStorage.setItem('budgetSuggestion', this.budgetSuggestion);
      
      // Parse it into a Budget object if needed
      this.aiBudget = this.suggestionService.parseAiGeneratedBudgetResponse(this.budgetSuggestion, this.userId);
    } catch (error) {
      this.errorMessage = 'An error occurred while regenerating the budget suggestion.';
    } finally {
      this.loading = false;
    }
  }

  onAddToBudget(): void {
    this.aiBudget = this.suggestionService.parseAiGeneratedBudgetResponse(this.suggestion, this.userId);
    console.log('Budget suggestion added:', this.aiBudget);
  
    this.budgetService.createBudget(this.aiBudget).subscribe(() => {
      // Remove the suggestion from localStorage after adding it to the budget
      localStorage.removeItem('budgetSuggestion');
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
