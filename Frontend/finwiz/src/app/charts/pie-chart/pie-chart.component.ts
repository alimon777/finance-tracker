import { Component, AfterViewInit, ElementRef, ViewChild, Input, SimpleChanges, OnChanges } from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { Expenditure } from 'src/app/models/expenditure';

Chart.register(...registerables);

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.css']
})
export class PieChartComponent implements AfterViewInit, OnChanges {
  @ViewChild('pieChart') pieChartRef!: ElementRef;

  @Input() pieChartData: Expenditure = {
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

  pieChart!: Chart<'pie', number[], string>;

  ngAfterViewInit(): void {
    if (this.pieChartData.deposit.INCOME > 0 || this.pieChartData.withdrawTotal>0) {
      this.createPieChart();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['pieChartData'] && (this.pieChartData.deposit.INCOME > 0 || this.pieChartData.withdrawTotal>0)) {
      this.createPieChart(); // Re-create the pie chart when transactions input changes
    }
  }

  createPieChart() {
    // Check if there's an existing chart and destroy it before creating a new one
    if (this.pieChart) {
      this.pieChart.destroy();
    }
  
    // Define the categories for the pie chart
    const categories = ['INCOME', 'FOOD', 'HOUSING', 'ENTERTAINMENT', 'TRANSPORTATION'];
  
    // Count the transactions for each category
    let categoryCounts: number[] = [0, 0, 0, 0, 0];
    categoryCounts[0] = this.pieChartData.deposit.INCOME;
    categoryCounts[1] = this.pieChartData.withdraw.FOOD;
    categoryCounts[2] = this.pieChartData.withdraw.HOUSING;
    categoryCounts[3] = this.pieChartData.withdraw.ENTERTAINMENT;
    categoryCounts[4] = this.pieChartData.withdraw.TRANSPORTATION;
    // Prepare the chart data
    const pieChartData: ChartConfiguration<'pie', number[], string>['data'] = {
      labels: categories,
      datasets: [{
        data: categoryCounts,
        backgroundColor: ['#4CAF50', '#FFC107', '#F44336', '#9C27B0', '#2196F3']
      }]
    };
  
    // Chart options with label position at the bottom
    const pieChartOptions: ChartConfiguration<'pie', number[], string>['options'] = {
      responsive: true,
      plugins: {
        legend: {
          position: 'bottom', // Set the legend position to bottom
          labels: {
            boxWidth: 20, // Optional: Customize label box width
            padding: 15 // Optional: Add some space between the labels
          }
        }
      }
    };
  
    // Create the pie chart
    this.pieChart = new Chart(this.pieChartRef.nativeElement, {
      type: 'pie',
      data: pieChartData,
      options: pieChartOptions,
    });
  }
  
}
