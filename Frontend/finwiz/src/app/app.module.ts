import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CustomInterceptor } from './core/interceptors/custom.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AuthGuardService } from './core/guards/auth-guard.service';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';

import { DashboardModule } from './features/dashboard/dashboard.module';
import { ManageGoalsModule } from './features/manage-goals/manage-goals.module';
import { ManageBudgetsModule } from './features/manage-budgets/manage-budgets.module';
import { TransactionTrackingModule } from './features/transaction-tracking/transaction-tracking.module';
import { AuthModule } from './features/auth/auth.module';
import { SharedModule } from './shared/shared.module';
import { NewsModule } from './features/news/news.module';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    AuthModule,
    DashboardModule,
    ManageGoalsModule,
    ManageBudgetsModule,
    TransactionTrackingModule,
    NewsModule,
    SharedModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    CommonModule,
    MatSnackBarModule,
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

