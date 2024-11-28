import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LineChartComponent } from './charts/line-chart/line-chart.component';
import { HomeComponent } from './home/home.component';
import { PieChartComponent } from './charts/pie-chart/pie-chart.component';

@NgModule({
  declarations: [
    HomeComponent,
    LineChartComponent,
    PieChartComponent,
  ],
  imports: [
    CommonModule
  ]
})
export class DashboardModule { }
