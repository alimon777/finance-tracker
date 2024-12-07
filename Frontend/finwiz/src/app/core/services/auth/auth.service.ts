import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { StorageService } from '../storage/storage.service';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private apiUrl = environment.apiBaseUrl + '/auth';

  constructor(
    private http: HttpClient,
    public jwtHelper: JwtHelperService,
    private storageService: StorageService,
    private router: Router
  ) { }

  public isAuthenticated(): boolean {
    const token = this.storageService.getToken();
    return !this.jwtHelper.isTokenExpired(token);
  }

  // Login method to authenticate the user
  login(credentials: any): Observable<any> {
    console.log(credentials);
    return this.http.post(`${this.apiUrl}/login`, credentials)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Register method to create a new user
  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user)
      .pipe(
        catchError(this.handleError)
      );
  }

  getToken(): string | null {
    return this.storageService.getToken();
  }

  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) {
      return true;
    }

    const tokenPayload = this.decodeToken(token);
    const expirationTime = tokenPayload.exp * 1000; // JWT expiration is in seconds, convert to milliseconds
    const currentTime = Date.now();
    console.log(expirationTime, currentTime);
    return currentTime >= expirationTime;
  }

  // Decode the JWT token and extract its payload
  decodeToken(token: string): any {
    const payload = token.split('.')[1];
    const decodedPayload = atob(payload);  // Decode from base64
    return JSON.parse(decodedPayload);  // Parse as JSON
  }


  // Clear token and log the user out
  clearToken(): void {
    this.storageService.clearStorage();
    this.router.navigate(['/login']);
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
          errorMessage = `Server returned code: ${error.status}, error message is: ${error.message}`;
      }
    }

    // console.error(`Error: ${errorMessage}`);
    return throwError(errorMessage);
  }
}
