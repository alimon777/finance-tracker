import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Budget } from 'src/app/models/budget';
import { StorageService } from '../storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class BudgetService {
  private apiUrl = 'http://localhost:8000/api/budgets'; 

  constructor(private http: HttpClient, private storageService:StorageService) {}
  

  createBudget(budget: Budget): Observable<Budget> {
    const userDetails = this.storageService.fetchUserDetails();
    budget.username = userDetails.username;
    budget.email = userDetails.email;
    return this.http.post<Budget>(this.apiUrl, budget).pipe(
      catchError(this.handleError)
    );
  }

  getBudgetsByUserId(userId: number): Observable<Budget[]> {
    return this.http.get<Budget[]>(`${this.apiUrl}/${userId}`).pipe(
      catchError(this.handleError)
    );
  }

  deleteBudget(userId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      if (error.status === 400) {
        errorMessage = 'Bad Request: ' + (error.error?.message || 'Invalid request.');
      } else if (error.status === 404) {
        errorMessage = 'Resource not found: ' + (error.error?.message || 'Requested resource not found.');
      } else if (error.status === 409) {
        errorMessage = 'Conflict: ' + (error.error?.message || 'Data integrity issue.');
      } else if (error.status === 500) {
        errorMessage = 'Server Error: ' + (error.error?.message || 'An unexpected error occurred.');
      }
    }
    return throwError(errorMessage);
  }
}
