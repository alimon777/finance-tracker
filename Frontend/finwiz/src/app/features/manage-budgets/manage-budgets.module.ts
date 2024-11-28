import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { BudgetComponent } from './budget/budget.component';


@NgModule({
  declarations: [
    BudgetComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ]
})
export class ManageBudgetsModule { }
