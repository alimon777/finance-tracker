import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8000/auth'; // Your Spring Boot backend URL

  constructor(private http: HttpClient) {}

  // Login method to authenticate the user
  login(credentials: any): Observable<any> {
    console.log(credentials);
    return this.http.post(`${this.baseUrl}/login`, credentials)
    .pipe(
      catchError(this.handleError)
    );
  }

  // Register method to create a new user
  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user)
    .pipe(
      catchError(this.handleError)
    );
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

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An unknown error occurred!';

    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      // Server-side error
      switch (error.status) {
        case 0:
          // No response from the server
          errorMessage = 'No response from server. Please check your connection.';
          break;
        case 400:
          if (error.error === 'Username already exists') {
            errorMessage = 'Registration failed: Username already exists.';
          } else {
            errorMessage = 'Invalid request. Please check your input.';
          }
          break;
        case 404:
          errorMessage = 'Resource not found or invalid endpoint.';
          break;
        case 401:
          errorMessage = 'Authentication failed: Invalid credentials.';
          break;
        default:
          // General server error
          errorMessage = `Server returned code: ${error.status}, error message is: ${error.message}`;
      }
    }

    // Log error to console (optional)
    console.error(`Error: ${errorMessage}`);
    // Throw an observable with a user-facing error message
    return throwError(errorMessage);
  }
}
