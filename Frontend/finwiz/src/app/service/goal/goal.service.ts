import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Goal } from 'src/app/models/goal';

@Injectable({
  providedIn: 'root'
})
export class GoalService {

  private readonly baseUrl = 'http://localhost:8300/goals';

  constructor(private http: HttpClient) { }
  
  createGoal(goalDto: Goal): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/create`, goalDto);
  }

  getAllGoals(userId:number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`);
  }

  deleteGoal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
  }
}
