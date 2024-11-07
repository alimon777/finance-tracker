import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Transaction } from 'src/app/models/transaction';
import { TransactionService } from 'src/app/service/transaction/transaction.service';
import { ExpenditureService } from 'src/app/service/expenditure/expenditure.service';
import { Expenditure } from 'src/app/models/expenditure';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {
  userId: number = 0;
  transactions: Transaction[] = [];
  selectedExpenditure: string = 'weekly'; // Default option set here
  expenditureSummary: any;
  pieChartData: Expenditure = {
    deposit: {
      INCOME: 0.0
    },
    withdraw: {
      ENTERTAINMENT: 0.0,
      TRANSPORTATION: 0.0,
      HOUSING: 0.0,
      FOOD: 0.0
    },
    withdrawTotal: 0.0
  };

  constructor(
    private http: HttpClient,
    private transactionService: TransactionService,
    private expenditureService: ExpenditureService
  ) {}

  ngOnInit(): void {
    this.loadUserId();
    this.loadTransactions();
    this.fetchExpenditureSummary();
  }

  selectExpenditure(option: string) {
    this.selectedExpenditure = option;
    this.fetchExpenditureSummary();
    this.pieChartData = this.expenditureSummary?.[option]; // Set the pie chart data
  }

  private fetchExpenditureSummary(): void {
    const userId = Number(localStorage.getItem('userId')); // Retrieve userId from local storage

    this.expenditureService.getExpenditureSummary(userId).subscribe(
      (data) => {
        this.expenditureSummary = data;
        // After fetching the data, set the default pie chart data
        if (this.selectedExpenditure) {
          this.pieChartData = this.expenditureSummary?.[this.selectedExpenditure];
        }
      },
      (error) => {
        console.error('Error fetching expenditure summary:', error);
      }
    );
  }

  loadUserId(): void {
    const storedUserId = localStorage.getItem('userId');
    this.userId = storedUserId ? +storedUserId : 0;
  }

  loadTransactions(): void {
    if (this.userId) {
      this.transactionService.getTransactions(this.userId).subscribe({
        next: (response: Transaction[]) => {
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
