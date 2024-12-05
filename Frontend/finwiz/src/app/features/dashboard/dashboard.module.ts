import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LineChartComponent } from './charts/line-chart/line-chart.component';
import { HomeComponent } from './home/home.component';
import { PieChartComponent } from './charts/pie-chart/pie-chart.component';
import { DashboardService } from './dashboard.service';

@NgModule({
  declarations: [
    HomeComponent,
    LineChartComponent,
    PieChartComponent,
  ],
  imports: [
    CommonModule
  ],
  providers: [
    DashboardService
  ]
})

export class DashboardModule { }
