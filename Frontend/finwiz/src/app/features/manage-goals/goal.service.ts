import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Goal } from 'src/app/features/manage-goals/goal.model';
import { environment } from 'src/environments/environment';

@Injectable()

export class GoalService {

  private apiUrl = environment.apiBaseUrl + '/api/goals';

  constructor(private http: HttpClient) { }

  createGoal(goalDto: Goal): Observable<any> {
    return this.http.post<any>(this.apiUrl, goalDto)
      .pipe(catchError(error => this.handleError(error, 'Failed to create goal. Please try again later.')));
  }

  getAllGoals(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl)
      .pipe(catchError(error => this.handleError(error, 'Failed to fetch goals. Please try again later.')));
  }

  deleteGoal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}${id}`)
      .pipe(catchError(error => this.handleError(error, 'Failed to delete goal. Please try again later.')));
  }

  updateGoal(goal: Goal): Observable<Goal> {
    return this.http.put<Goal>(`${this.apiUrl}/${goal['id']}`, goal)
      .pipe(catchError(error => this.handleError(error, 'Failed to update goal. Please try again later.')));
  }

  private handleError(error: HttpErrorResponse, customMessage?: string) {
    let errorMessage = customMessage || 'An unknown error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client error: ${error.error.message}`;
    } else {
      errorMessage = `Server error (code ${error.status}): ${error.message}`;
    }
    console.error("GoalService error:", errorMessage); // Centralized logging
    return throwError(() => new Error(customMessage));
  }
}
