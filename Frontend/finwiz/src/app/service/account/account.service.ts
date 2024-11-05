import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from 'src/app/models/account'; // Adjust the import path as needed
import { CustomResponse } from 'src/app/models/custom-rseponse';// Define CustomResponse if needed

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8000/test/accounts'; // Base URL of Spring Boot server

  constructor(private http: HttpClient) {}

  // Create a new account
  addAccount(account: Account): Observable<CustomResponse<Account>> {
    return this.http.post<CustomResponse<Account>>(this.apiUrl, account);
  }

  // Get all accounts for a specific user by userId
  getAllAccounts(userId: number): Observable<CustomResponse<Account[]>> {
    return this.http.get<CustomResponse<Account[]>>(`${this.apiUrl}/user/${userId}`);
  }

  // Update an account by accountId
  updateAccount(accountId: number, account: Account): Observable<CustomResponse<Account>> {
    return this.http.put<CustomResponse<Account>>(`${this.apiUrl}/${accountId}`, account);
  }

  // Delete an account by accountId
  deleteAccount(accountId: number): Observable<CustomResponse<string>> {
    return this.http.delete<CustomResponse<string>>(`${this.apiUrl}/${accountId}`);
  }
}
