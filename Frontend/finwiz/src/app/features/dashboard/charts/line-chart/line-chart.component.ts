import { Component, AfterViewInit, ElementRef, ViewChild, Input, SimpleChanges } from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { IncomeDepositDTO } from 'src/app/shared/models/income-deposit';

Chart.register(...registerables);

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements AfterViewInit {
  @ViewChild('lineChart') lineChartRef!: ElementRef;

  @Input() lineChartData: IncomeDepositDTO[] = [];

  lineChart!: Chart<'line', number[], string>;

  ngAfterViewInit(): void {
    this.createLineChart();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['lineChartData'] && this.lineChartData.length > 0) {
      this.createLineChart(); // Recreate chart if lineChartData input changes
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
    this.lineChart = new Chart(this.lineChartRef.nativeElement, {
      type: 'line',
      data: lineChartData,
      options: lineChartOptions,
    });
  }

}
