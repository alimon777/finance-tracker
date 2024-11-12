import { Component, OnInit } from '@angular/core';
import { GoalService } from 'src/app/service/goal/goal.service';
import { Goal } from 'src/app/models/goal';
import { StorageService } from 'src/app/service/storage/storage.service';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

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
    value: null,
    description: '',
    durationInMonths: null,
    startDate: new Date()
  };
  goals: any[] = [];
  goalIdToDelete: number | null = null;
  selectedGoal: any | null = null;
  showEditModal: boolean = false;

 


  constructor(private goalService: GoalService, private storageService: StorageService, private snackbar: MatSnackBar) { }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();

    if (this.userId) {
      this.loadGoals();
    } else {
      console.error('User ID not found in local storage');

    }
  }
  formSubmitted = false;

  onSubmit(form: NgForm): void {
    this.formSubmitted = true;  // Set flag to true when save is attempted

    if (form.invalid) {
      this.snackbar.open('Please fill in all fields correctly', 'Close', {
        duration: 3000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
      });
      return;  // Early return if form is invalid
    }

    this.goal.userId = this.userId;

    this.goalService.createGoal(this.goal).subscribe(
      (newGoal) => {
        this.goals.push(newGoal);
        form.resetForm();
        this.formSubmitted = false;  // Reset the flag after a successful submission
        this.snackbar.open('Goal Created Successfully', 'Close', {
          duration: 3000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      },
      (error) => {
        console.error('Error saving goal:', error);
        this.snackbar.open('Error saving goal', 'Close', {
          duration: 3000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      }
    );
  }
  
  
  loadGoals(): void {
    this.goalService.getAllGoals(this.userId).subscribe(
      (data: Goal[]) => {
        this.goals = data;
      },
      (error) => {
        console.error('Error loading goals:', error);
        this.snackbar.open('Failed to load goals. Please try again later.', 'Close', {
          duration: 3000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      }
    );
  }
  

  // confirmDelete(goalId: number): void {
  //   const confirmDelete = window.confirm('Are you sure you want to delete this goal?');
  //   if (confirmDelete) {
  //     this.deleteGoal(goalId);
  //   }
  // }

  deleteGoal(goalId: number): void {
    this.goalService.deleteGoal(goalId).subscribe( {
      next: () =>{
        this.goals = this.goals.filter(goal => goal.id !== goalId);
      this.loadGoals();
      this.snackbar.open('Goal Deleted Successfully', 'Close', {
        duration: 3000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
      })
      },
      error: () => {
        console.error('Error deleting goal:');
        this.snackbar.open('Error while deleting goal', 'Close', {
          duration: 3000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        })
      }
    });
  }
  

  // Calculate remaining days for each goal
  calculateDaysLeft(goal: Goal): number {
    const currentDate = new Date();
    const createdDate = new Date(goal.startDate);
    if (goal.durationInMonths === null) {
      return 0;  // If durationInMonths is null, return 0 days left
    }
    const endDate = new Date(createdDate.setMonth(createdDate.getMonth() + goal.durationInMonths));
    
    // Calculate the difference in time
    const timeDiff = endDate.getTime() - currentDate.getTime();
    const daysLeft = Math.ceil(timeDiff / (1000 * 3600 * 24));  // Convert time to days

    return daysLeft >= 0 ? daysLeft : 0;  // Return 0 if the goal has passed
  }

  resetForm(): void {
    this.goal = {
      goalName: '',
      value:null,
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
}