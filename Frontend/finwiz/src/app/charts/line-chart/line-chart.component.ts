import { Component, AfterViewInit, ElementRef, ViewChild, Input, SimpleChanges } from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements AfterViewInit {
  @ViewChild('lineChart') lineChartRef!: ElementRef;

  @Input() transactions: any[] = []; // Expecting an array of transactions as input

  lineChart!: Chart<'line', number[], string>;

  ngAfterViewInit(): void {
    this.createLineChart();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['transactions'] && this.transactions.length > 0) {
      this.createLineChart(); // Recreate chart if transactions input changes
    }
  }

  createLineChart() {
    // Check if there is an existing chart and destroy it
    if (this.lineChart) {
      this.lineChart.destroy();
    }
  
    // Prepare the data
    const labels = this.transactions.map(t => {
      const date = new Date(t.transactionDate);
      return date.toLocaleDateString();
    });
  
    const depositAmounts = this.transactions
      .filter(t => t.transactionType === 'DEPOSIT')
      .map(t => t.amount);
  
    const withdrawalAmounts = this.transactions
      .filter(t => t.transactionType === 'WITHDRAW')
      .map(t => t.amount);
  
    const lineChartData: ChartConfiguration<'line', number[], string>['data'] = {
      labels: labels,
      datasets: [
        {
          label: 'Deposits',
          data: this.transactions.map(t => (t.transactionType === 'DEPOSIT' ? t.amount : 0)),
          borderColor: 'blue',
          fill: false,
          tension: 0.1,
        },
        {
          label: 'Withdrawals',
          data: this.transactions.map(t => (t.transactionType === 'WITHDRAW' ? t.amount : 0)),
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
