import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Goal } from 'src/app/models/goal';
import { GoalService } from 'src/app/service/goal/goal.service';

@Component({
  selector: 'app-update-goal-modal',
  templateUrl: './update-goal-modal.component.html',
  styleUrls: ['./update-goal-modal.component.css']
})
export class UpdateGoalModalComponent {

  @Input()
  goal!: Goal;
  @Output() close = new EventEmitter<Goal | null>();

  isVisible = true;

  constructor(private goalService: GoalService) {}

  onSubmit(): void {
    this.goalService.updateGoal(this.goal).subscribe(
      (updatedGoal) => {
        this.close.emit(updatedGoal);
      },
      (error) => {
        console.error('Error updating goal:', error);
        this.close.emit(null);
      }
    );
  }

  onCancel(): void {
    this.isVisible = false;
    this.close.emit(null);
  }
}
