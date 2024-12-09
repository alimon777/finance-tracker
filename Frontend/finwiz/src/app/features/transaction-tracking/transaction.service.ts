import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { CustomResponse } from 'src/app/features/transaction-tracking/models/custom-response.model';
import { environment } from 'src/environments/environment';
import { Account } from 'src/app/features/transaction-tracking/models/account.model';
import { Transaction } from 'src/app/features/transaction-tracking/models/transaction.model';

@Injectable()

export class TransactionService {
  private apiUrl = environment.apiBaseUrl + '/api/transactions';

  constructor(private http: HttpClient) { }

  addAccount(account: Account): Observable<CustomResponse<Account>> {
    return this.http.post<CustomResponse<Account>>(`${this.apiUrl}/accounts`, account).pipe(
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

  getAllAccounts(): Observable<CustomResponse<Account[]>> {
    return this.http.get<CustomResponse<Account[]>>(`${this.apiUrl}/accounts`).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred while adding the account.';
        if (error.status === 404) {
          errorMessage = 'No Accounts available';
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  deleteAccount(accountId: number): Observable<CustomResponse<string>> {
    return this.http.delete<CustomResponse<string>>(`${this.apiUrl}/accounts/${accountId}`);
  }

  addTransaction(transaction: Transaction): Observable<any> {
    return this.http.post(this.apiUrl, transaction).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred while adding the transaction.';
        if (error.status === 400) {
          errorMessage = 'Insufficient Balance!';
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  addMultipleTransactions(transactions: Transaction[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/multiple`, transactions).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred while adding the transactions.';
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  getTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(this.apiUrl);
  }
}