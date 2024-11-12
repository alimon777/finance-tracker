import { Component, OnInit } from '@angular/core';
import { GoalService } from 'src/app/service/goal/goal.service';
import { Goal } from 'src/app/models/goal';
import { StorageService } from 'src/app/service/storage/storage.service';

@Component({
  selector: 'app-goal',
  templateUrl: './goal.component.html',
  styleUrls: ['./goal.component.css']
})
export class GoalComponent implements OnInit {
  userId:number=0;
  user: any;

  goal: Goal = {
    goalName: '',
    value: 0,
    description: '',
    durationInMonths: 0,
    startDate: new Date()
  };
  goals: any[] = [];
  goalIdToDelete: number | null = null;
  selectedGoal: any | null = null;
  showEditModal: boolean = false;

  constructor(private goalService: GoalService, private storageService: StorageService) { }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();

    if (this.userId) {
      this.loadGoals();
    } else {
      console.error('User ID not found in local storage');

    }
  }

  onSubmit(): void {
    this.goal.userId=this.userId;
    if (!this.goal.goalName || this.goal.value <= 0 || this.goal.durationInMonths <= 0) {
      alert('All fields are required, and value/duration must be greater than zero!');
      return;
    }

    this.goalService.createGoal(this.goal).subscribe(
      (newGoal) => {
        this.goals.push(newGoal);
        this.resetForm();
      },
      (error) => {
        console.error('Error saving goal:', error);
      }
    );
  }

  loadGoals(): void {
    this.goalService.getAllGoals(this.userId).subscribe((data: Goal[]) => {
      this.goals = data;
  });
}

  confirmDelete(goalId: number): void {
    const confirmDelete = window.confirm('Are you sure you want to delete this goal?');
    if (confirmDelete) {
      this.deleteGoal(goalId);
    }
  }

  deleteGoal(goalId: number): void {
    this.goalService.deleteGoal(goalId).subscribe(() => {
      // Filter out the deleted goal from the local list of goals
      this.goals = this.goals.filter(goal => goal.id !== goalId);
      this.loadGoals();
    });
  }
  

  // Calculate remaining days for each goal
  calculateDaysLeft(goal: Goal): number {
    const currentDate = new Date();
    const createdDate = new Date(goal.startDate);
    const endDate = new Date(createdDate.setMonth(createdDate.getMonth() + goal.durationInMonths));
    
    // Calculate the difference in time
    const timeDiff = endDate.getTime() - currentDate.getTime();
    const daysLeft = Math.ceil(timeDiff / (1000 * 3600 * 24));  // Convert time to days

    return daysLeft >= 0 ? daysLeft : 0;  // Return 0 if the goal has passed
  }

  resetForm(): void {
    this.goal = {
      goalName: '',
      value: 0,
      description: '',
      durationInMonths: 0,
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
}