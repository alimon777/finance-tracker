import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { Goal } from 'src/app/models/goal';

@Injectable({
  providedIn: 'root'
})
export class GoalService {

  private readonly baseUrl = 'http://localhost:8000/api/goals';

  constructor(private http: HttpClient) { }

  createGoal(goalDto: Goal): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/create`, goalDto)
    .pipe(
      catchError(error => this.handleError(error, 'Failed to create goal. Please try again later.'))
    );
  }

  getAllGoals(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`)
    .pipe(
      catchError(error => this.handleError(error, 'Failed to fetch goals. Please try again later.'))
    );
  }

  deleteGoal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`).pipe(
      catchError(error => this.handleError(error, 'Failed to delete goal. Please try again later.'))
    );
  }

  updateGoal(goal: Goal): Observable<Goal> {
    return this.http.put<Goal>(`${this.baseUrl}/update/${goal['id']}`, goal)
    .pipe(
      catchError(error => this.handleError(error, 'Failed to update goal. Please try again later.'))
    );
  }

  private handleError(error: HttpErrorResponse, customMessage?: string) {
    let errorMessage = customMessage || 'An unknown error occurred';
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Client error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = `Server error (code ${error.status}): ${error.message}`;
    }
    // Log the error to the console
    console.error(errorMessage);
    return throwError(errorMessage);
  }
}
