import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Goal } from 'src/app/shared/models/goal';
import { GoalService } from 'src/app/shared/services/goal/goal.service';
import { SnackbarService } from 'src/app/core/services/snackbar/snackbar.service';

@Component({
  selector: 'app-update-goal-modal',
  templateUrl: './update-goal-modal.component.html',
  styleUrls: ['./update-goal-modal.component.css']
})
export class UpdateGoalModalComponent {

  @Input() goal!: Goal;
  @Output() close = new EventEmitter<Goal | null>();

  isVisible = true;

  constructor(private goalService: GoalService, private snackbarService: SnackbarService) {}

  onSubmit(): void {
    this.goalService.updateGoal(this.goal).subscribe(
      (updatedGoal) => {
        this.snackbarService.show('Goal updated successfully'); // Show success message
        this.close.emit(updatedGoal);
      },
      (error) => {
        this.snackbarService.show(error.message); 
        this.close.emit(null);
      }
    );
  }

  onCancel(): void {
    this.isVisible = false;
    this.close.emit(null);
  }
}
