import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Budget } from 'src/app/features/manage-budgets/budget.model';
import { AiSuggestion } from 'src/app/features/manage-budgets/budget.model';

@Injectable()

export class BudgetService {

  constructor(private http: HttpClient) { }

  private aiApiUrl = environment.apiBaseUrl + '/api/gemini/suggestion';
  private budgetApiUrl = environment.apiBaseUrl + '/api/budgets';

  getAiSuggestion(): Observable<AiSuggestion> {
    return this.http.get<AiSuggestion>(this.aiApiUrl);
  }

  createBudget(budget: Budget): Observable<Budget> {
    return this.http.post<Budget>(this.budgetApiUrl, budget).pipe(
      catchError(this.handleError)
    );
  }

  getBudgetsByUserId(): Observable<Budget[]> {
    return this.http.get<Budget[]>(this.budgetApiUrl).pipe(
      catchError(this.handleError)
    );
  }

  deleteBudget(id: number): Observable<void> {
    return this.http.delete<void>(`${this.budgetApiUrl}/${id}`).pipe(
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
