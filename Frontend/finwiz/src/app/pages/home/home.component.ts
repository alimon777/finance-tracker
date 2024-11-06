import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Transaction } from 'src/app/models/transaction';
import { TransactionService } from 'src/app/service/transaction/transaction.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {
  userId:number = 0;
  transactions: Transaction[] = [ ];
  constructor(private http: HttpClient, private transactionService: TransactionService) { }

  ngOnInit(): void {
    this.loadUserId();
    this.loadTransactions();
  }
  loadUserId(): void {
    const storedUserId = localStorage.getItem('userId');
    this.userId = storedUserId ? +storedUserId : 0; 
  }
  loadTransactions(): void {
    if (this.userId) {
      this.transactionService.getTransactions(this.userId).subscribe({
        next: (response: Transaction[]) => { // Specify the response type here
          // Sort transactions by transactionDate in ascending order
          this.transactions = response.sort((a, b) => {
            const dateA = new Date(a.transactionDate); // Convert string to Date object
            const dateB = new Date(b.transactionDate); // Convert string to Date object
            return dateA.getTime() - dateB.getTime(); // Compare timestamps
          });
  
          console.log(this.transactions); // Ensure the transactions are sorted correctly
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading transactions:', error.message);
        },
      });
    } else {
      console.warn('User ID not found in local storage.');
    }
  }
  
}
  