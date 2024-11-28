import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { GoalComponent } from './goal/goal.component';
import { UpdateGoalModalComponent } from './update-goal-modal/update-goal-modal.component';

@NgModule({
  declarations: [
    GoalComponent,
    UpdateGoalModalComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
  ]
})
export class ManageGoalsModule { }
