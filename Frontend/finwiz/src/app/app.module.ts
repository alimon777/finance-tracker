import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { HomeComponent } from './pages/home/home.component';
import { LayoutComponent } from './pages/layout/layout.component';
import { CustomInterceptor } from './service/custom/custom.interceptor';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { LandingComponent } from './pages/landing/landing.component';
import { TransactionComponent } from './pages/transaction/transaction.component';
import { BudgetComponent } from './pages/budget/budget.component';
import { GoalComponent } from './pages/goal/goal.component';
import { AiComponent } from './pages/ai/ai.component';
import { LineChartComponent } from './charts/line-chart/line-chart.component';
import { PieChartComponent } from './charts/pie-chart/pie-chart.component';
import { NewsComponent } from './pages/news/news.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { UpdateGoalModalComponent } from './pages/update-goal-modal/update-goal-modal.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AuthGuardService } from './service/auth/auth-guard.service';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    PageNotFoundComponent,
    LandingComponent,
    TransactionComponent,
    BudgetComponent,
    GoalComponent,
    AiComponent,
    LineChartComponent,
    PieChartComponent,
    NewsComponent,
    UpdateGoalModalComponent,

  ],
  imports: [
    LayoutComponent,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    CommonModule,
    MatSnackBarModule

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CustomInterceptor,
      multi: true
    },
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    JwtHelperService,
    AuthGuardService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

