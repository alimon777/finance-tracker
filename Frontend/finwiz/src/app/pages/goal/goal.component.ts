import { Component, OnInit } from '@angular/core';
import { GoalService } from 'src/app/service/goal/goal.service';
import { Goal } from 'src/app/models/goal';
import { StorageService } from 'src/app/service/storage/storage.service';
import { NgForm } from '@angular/forms';
import { SnackbarService } from 'src/app/service/snackbar/snackbar.service';

@Component({
  selector: 'app-goal',
  templateUrl: './goal.component.html',
  styleUrls: ['./goal.component.css']
})
export class GoalComponent implements OnInit {
  userId: number = 0;
  goals: any[] = [];
  formSubmitted = false;
  goalIdToDelete: number | null = null;
  selectedGoal: any | null = null;
  showEditModal: boolean = false;

  goal: Goal = {
    goalName: '',
    value: null,
    description: '',
    durationInMonths: null,
    startDate: new Date()
  };

  constructor(
    private goalService: GoalService,
    private storageService: StorageService,
    private snackbarService: SnackbarService
  ) { }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
    if (this.userId) {
      this.loadGoals();
    } else {
      console.error('User ID not found in local storage');
    }
  }

  onSubmit(form: NgForm): void {
    this.formSubmitted = true;
    if (form.invalid) {
      this.snackbarService.show('Please fill in all fields correctly');
      return;
    }

    this.goal.userId = this.userId;

    this.goalService.createGoal(this.goal).subscribe(
      (newGoal) => {
        this.goals.push(newGoal);
        form.resetForm();
        this.formSubmitted = false;
        this.snackbarService.show('Goal created successfully');
      },
      (error) => {
        this.snackbarService.show(error.message);
      }
    );
  }

  loadGoals(): void {
    this.goalService.getAllGoals(this.userId).subscribe({
      next: (data: Goal[]) => {
        this.goals = data;
      },
      error: (error) => {
        //this.snackbarService.show(error.message);
      }
    });
  }

  deleteGoal(goalId: number): void {
    this.goalService.deleteGoal(goalId).subscribe({
      next: () => {
        this.goals = this.goals.filter(goal => goal.id !== goalId);
        this.loadGoals();
        this.snackbarService.show('Goal deleted successfully');
      },
      error: (error) => {
        this.snackbarService.show(error.message);
      }
    });
  }

  resetForm(): void {
    this.goal = {
      goalName: '',
      value: null,
      description: '',
      durationInMonths: null,
      startDate: new Date()
    };
  }

  openEditModal(goal: Goal): void {
    this.selectedGoal = { ...goal };
    this.showEditModal = true;
  }

  onModalClose(updatedGoal: Goal | null): void {
    if (updatedGoal) {
      const index = this.goals.findIndex(goal => goal.id === updatedGoal['id']);
      if (index !== -1) {
        this.goals[index] = updatedGoal;
      }
    }
    this.showEditModal = false;
  }

  calculateDaysLeft(goal: Goal): number {
    const currentDate = new Date();
    const createdDate = new Date(goal.startDate);
    if (goal.durationInMonths === null) {
      return 0;
    }
    const endDate = new Date(createdDate.setMonth(createdDate.getMonth() + goal.durationInMonths));
    const timeDiff = endDate.getTime() - currentDate.getTime();
    const daysLeft = Math.ceil(timeDiff / (1000 * 3600 * 24));
    return daysLeft >= 0 ? daysLeft : 0;
  }
}
