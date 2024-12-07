import { Component, OnInit } from '@angular/core';
import { Transaction } from 'src/app/features/transaction-tracking/models/transaction.model';
import { DashboardService } from '../dashboard.service';
import { Expenditure } from 'src/app/features/dashboard/expenditure.model';
import { IncomeDepositDTO } from 'src/app/features/dashboard/income-deposit.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {

  transactions: Transaction[] = [];
  periodLabel: string = 'weekly';
  expenditureSummary: any;
  lineChartData: IncomeDepositDTO[] = [];
  chartType: 'line' | 'pie' = 'pie';
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
    private dashboardService: DashboardService,
  ) { }

  ngOnInit(): void {
    this.fetchExpenditureSummary();
    this.fetchIncomeDepositSummary();
  }

  toggleChartType(type: 'line' | 'pie') {
    this.chartType = type;
  }

  toggleDropdown(period: 'weekly' | 'monthly' | 'yearly') {
    this.periodLabel = period;
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

    this.dashboardService.getIncomeDepositSummary(this.periodLabel).subscribe(
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

    this.dashboardService.getExpenditureSummary().subscribe(
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
