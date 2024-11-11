import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Budget } from 'src/app/models/budget';// Create a model for Budget

@Injectable({
  providedIn: 'root'
})
export class BudgetService {
  private apiUrl = 'http://localhost:8000/api/budgets'; // Adjust the URL based on your backend

  constructor(private http: HttpClient) {}

  createBudget(budget: Budget): Observable<Budget> {
    return this.http.post<Budget>(this.apiUrl, budget);
  }

  getBudgetsByUserId(userId: number): Observable<Budget[]> {
    return this.http.get<Budget[]>(`${this.apiUrl}/${userId}`);
  }

  updateBudget(userId: number, id: number, budget: Budget): Observable<Budget> {
    return this.http.put<Budget>(`${this.apiUrl}/${userId}/${id}`, budget);
  }

  deleteBudget(userId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}/${id}`);
  }
}
