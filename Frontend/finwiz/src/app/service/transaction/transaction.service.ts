import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { Transaction } from 'src/app/models/transaction'; 

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private baseUrl = 'http://localhost:8000/api/transactions'; 

  constructor(private http: HttpClient) {}

  addTransaction(transaction: Transaction): Observable<any> {
    return this.http.post(`${this.baseUrl}`, transaction).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred while adding the account.';
        if (error.status === 400) {
          errorMessage = 'Insufficient Balance!';
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }

  getTransactions(userId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.baseUrl}/${userId}`);
  }
}
