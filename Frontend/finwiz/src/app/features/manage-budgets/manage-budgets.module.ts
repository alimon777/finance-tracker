import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { BudgetComponent } from './budget/budget.component';
import { AddBudgetComponent } from './add-budget/add-budget.component';
import { AiSuggestionComponent } from './ai-suggestion/ai-suggestion.component';
import { BudgetService } from './budget.service';

@NgModule({
  declarations: [
    BudgetComponent,
    AddBudgetComponent,
    AiSuggestionComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
  providers: [
    BudgetService,
  ]
})

export class ManageBudgetsModule { }
