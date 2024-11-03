import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8099'; // Your Spring Boot backend URL

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> {
    console.log(credentials);
    return this.http.post(`${this.baseUrl}/login`, credentials, { responseType: 'text' });
  }
  

  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user);
  }

  // Method to store token in local storage
  storeToken(token: string) {
    localStorage.setItem('token', token);
  }

  // Method to retrieve token from local storage
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Method to clear token from local storage
  clearToken() {
    localStorage.removeItem('token');
  }
}
