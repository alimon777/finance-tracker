import { Component, OnInit } from '@angular/core';
import { Transaction } from 'src/app/models/transaction';
import { ExpenditureService } from 'src/app/service/expenditure/expenditure.service';
import { Expenditure } from 'src/app/models/expenditure';
import { IncomeDepositDTO } from 'src/app/models/income-deposit';
import { StorageService } from 'src/app/service/storage/storage.service';

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

  chartType: 'line' | 'pie' = 'pie'; 

  constructor(
    private expenditureService: ExpenditureService,
    private storageService: StorageService
  ) {}

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
    this.fetchExpenditureSummary();
    this.fetchIncomeDepositSummary();
  }

  toggleChartType(type: 'line' | 'pie') {
    this.chartType = type;
  }

  toggleDropdown(period: 'weekly' | 'monthly' | 'yearly') {
    this.periodLabel=period;
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

  private fetchIncomeDepositSummary(): void {

    this.expenditureService.getIncomeDepositSummary(this.userId, this.periodLabel).subscribe(
      (data) => {
        if (this.periodLabel) {
          this.lineChartData = data;
        }
      },
      (error) => {
        console.error('Error fetching income deposit summary:', error);
      }
    );
  }

  private fetchExpenditureSummary(): void {

    this.expenditureService.getExpenditureSummary(this.userId).subscribe(
      (data) => {
        this.expenditureSummary = data;
        if (this.periodLabel) {
          this.pieChartData = this.expenditureSummary?.[this.periodLabel];
        }
      },
      (error) => {
        console.error('Error fetching expenditure summary:', error);
      }
    );
  }
}
