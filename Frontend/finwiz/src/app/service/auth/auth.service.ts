import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8000/auth'; // Your Spring Boot backend URL

  constructor(private http: HttpClient) {}

  // Login method to authenticate the user
  login(credentials: any): Observable<any> {
    console.log(credentials);
    return this.http.post(`${this.baseUrl}/login`, credentials);
  }

  // Register method to create a new user
  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user);
  }

  // Method to store token in local storage
  storeToken(response: { token: string; userId: string; username: string , email: string}) {
    localStorage.setItem('token', response.token);
    localStorage.setItem('userId', response.userId);
    localStorage.setItem('username', response.username);
    localStorage.setItem('email',response.email);
    localStorage.removeItem('budgetSuggestion'); 
  }

  // Method to retrieve token from local storage
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Method to clear token from local storage
  clearToken() {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('email');
    localStorage.removeItem('budgetSuggestion');
  }
}
