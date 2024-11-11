import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Transaction } from 'src/app/models/transaction';
import { TransactionService } from 'src/app/service/transaction/transaction.service';
import { ExpenditureService } from 'src/app/service/expenditure/expenditure.service';
import { Expenditure } from 'src/app/models/expenditure';
import { IncomeDepositDTO } from 'src/app/models/income-deposit';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {
  userId: number = 0;
  transactions: Transaction[] = [];
  periodLabel: string = 'weekly'; // Default option set here
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

  lineChartData: IncomeDepositDTO[] = [];

  // State to track which dropdown is expanded
  isWeeklyExpanded = true;
  isMonthlyExpanded = false;
  isYearlyExpanded = false;

  constructor(
    private http: HttpClient,
    private transactionService: TransactionService,
    private expenditureService: ExpenditureService
  ) {}

  ngOnInit(): void {
    this.loadUserId();
    this.fetchExpenditureSummary();
    this.fetchIncomeDepositSummary();
  }
  // Toggle the dropdown for the selected period
  toggleDropdown(period: 'weekly' | 'monthly' | 'yearly') {
    if (period === 'weekly' ) {
      this.isWeeklyExpanded = true;
      this.isMonthlyExpanded=false;
      this.isYearlyExpanded=false;
      this.periodLabel = 'weekly'; // Update the selected period when toggling
    } else if (period === 'monthly') {
      this.isWeeklyExpanded = false;
      this.isMonthlyExpanded=true;
      this.isYearlyExpanded=false;
      
      this.periodLabel = 'monthly';
    } else if (period === 'yearly') {
      this.isWeeklyExpanded = false;
      this.isMonthlyExpanded=false;
      this.isYearlyExpanded=true;
      this.periodLabel = 'yearly';
    }
    // Update expenditure summary and pie chart data when the dropdown is toggled
    this.fetchExpenditureSummary();
    this.fetchIncomeDepositSummary();
  }

  // Method to select the expenditure period and update the data
  selectExpenditure(option: string) {
    this.periodLabel = option;
    this.fetchExpenditureSummary();
    this.fetchIncomeDepositSummary();
    this.pieChartData = this.expenditureSummary?.[option]; // Set the pie chart data
  }

  // Fetch income deposit summary from the API
  private fetchIncomeDepositSummary(): void {
    const userId = Number(localStorage.getItem('userId')); // Retrieve userId from local storage

    this.expenditureService.getIncomeDepositSummary(userId, this.periodLabel).subscribe(
      (data) => {
        // After fetching the data, set the default line chart data
        if (this.periodLabel) {
          this.lineChartData = data;
        }
      },
      (error) => {
        console.error('Error fetching income deposit summary:', error);
      }
    );
  }
  // Fetch expenditure summary from the API
  private fetchExpenditureSummary(): void {
    const userId = Number(localStorage.getItem('userId')); // Retrieve userId from local storage

    this.expenditureService.getExpenditureSummary(userId).subscribe(
      (data) => {
        this.expenditureSummary = data;
        // After fetching the data, set the pie chart data
        if (this.periodLabel) {
          this.pieChartData = this.expenditureSummary?.[this.periodLabel];
        }
      },
      (error) => {
        console.error('Error fetching expenditure summary:', error);
      }
    );
  }
  // Load the userId from localStorage
  loadUserId(): void {
    const storedUserId = localStorage.getItem('userId');
    this.userId = storedUserId ? +storedUserId : 0;
  }
}
