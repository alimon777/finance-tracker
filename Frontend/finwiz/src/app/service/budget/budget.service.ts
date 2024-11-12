import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Budget } from 'src/app/models/budget';// Create a model for Budget
import { StorageService } from '../storage/storage.service';
import { UserDetails } from 'src/app/models/user-details';

@Injectable({
  providedIn: 'root'
})
export class BudgetService {
  private apiUrl = 'http://localhost:8000/api/budgets'; // Adjust the URL based on your backend

  constructor(private http: HttpClient, private storageService:StorageService) {}
  

  createBudget(budget: Budget): Observable<Budget> {
    const userDetails : UserDetails = this.storageService.fetchUserDetails();
    budget.username = userDetails.username;
    budget.email = userDetails.email;
    return this.http.post<Budget>(this.apiUrl, budget);
  }

  getBudgetsByUserId(userId: number): Observable<Budget[]> {
    return this.http.get<Budget[]>(`${this.apiUrl}/${userId}`);
  }

  deleteBudget(userId: number, id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}/${id}`);
  }
}
