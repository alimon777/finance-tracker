import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Account } from 'src/app/shared/models/account'
import { CustomResponse } from 'src/app/shared/models/custom-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = environment.apiBaseUrl+'/api/transactions/accounts'; 

  constructor(private http: HttpClient) {}

  // Create a new account
  addAccount(account: Account): Observable<CustomResponse<Account>> {
    return this.http.post<CustomResponse<Account>>(this.apiUrl, account).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred while adding the account.';
        if (error.status === 400) {
          errorMessage = 'Invalid input. Please check the provided account details.';
        } else if (error.status === 409) {
          errorMessage = 'An account with this account number already exists.';
        } else if (error.status === 500) {
          errorMessage = 'Server error. Please try again later.';
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }
  

  // Get all accounts for a specific user by userId
  getAllAccounts(userId: number): Observable<CustomResponse<Account[]>> {
    return this.http.get<CustomResponse<Account[]>>(`${this.apiUrl}/${userId}`).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred while adding the account.';
        if (error.status === 404) {
          errorMessage = 'No Accounts available';
        }
        return throwError(() => new Error(errorMessage));
      })
    );
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
