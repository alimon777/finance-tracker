import { Component, AfterViewInit, ElementRef, ViewChild, Input, SimpleChanges, OnChanges } from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { IncomeDepositDTO } from 'src/app/features/dashboard/income-deposit.model';

Chart.register(...registerables);

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements AfterViewInit, OnChanges {

  @ViewChild('lineChart') lineChartRef!: ElementRef;
  @Input() lineChartData: IncomeDepositDTO[] = [];

  lineChart!: Chart<'line', number[], string>;

  ngAfterViewInit(): void {
    // Only create the chart if the reference is available
    if (this.lineChartRef?.nativeElement) {
      this.createLineChart();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['lineChartData'] && this.lineChartData.length > 0) {
      // Avoid re-creating the chart if the reference isn't available yet
      if (this.lineChartRef?.nativeElement) {
        this.createLineChart(); // Recreate chart if lineChartData input changes
      }
    }
  }

  createLineChart() {
    // Check if there is an existing chart and destroy it
    if (this.lineChart) {
      this.lineChart.destroy();
    }

    // Prepare the data
    const labels = this.lineChartData.map(t => t.periodLabel);
    const depositAmounts = this.lineChartData.map(t => t.totalDepositsAmount);
    const withdrawAmounts = this.lineChartData.map(t => t.totalWithdrawalsAmount);

    const lineChartData: ChartConfiguration<'line', number[], string>['data'] = {
      labels: labels,
      datasets: [
        {
          label: 'Deposits',
          data: depositAmounts,
          borderColor: 'blue',
          fill: false,
          tension: 0.1,
        },
        {
          label: 'Withdrawals',
          data: withdrawAmounts,
          borderColor: 'red',
          fill: false,
          tension: 0.1,
        }
      ]
    };

    const lineChartOptions: ChartConfiguration<'line', number[], string>['options'] = {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
        }
      }
    };

    // Create the new chart
    if (this.lineChartRef?.nativeElement) {
      this.lineChart = new Chart(this.lineChartRef.nativeElement, {
        type: 'line',
        data: lineChartData,
        options: lineChartOptions,
      });
    } else {
      console.error('Chart reference is not available');
    }
  }
}
