import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from 'src/app/models/transaction'; // Import your Transaction model

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private baseUrl = 'http://localhost:8000/test/transactions'; // Adjust to your API base URL

  constructor(private http: HttpClient) {}

  addTransaction(transaction: Transaction): Observable<any> {
    return this.http.post(`${this.baseUrl}`, transaction);
  }

  getTransactions(userId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.baseUrl}/${userId}`);
  }
}
