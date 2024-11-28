import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { HomeComponent } from './features/dashboard/home/home.component';
import { LayoutComponent}from './shared/components/layout/layout.component'
import { PageNotFoundComponent } from './shared/components/page-not-found/page-not-found.component';
import { LandingComponent } from './shared/components/landing/landing.component';
import { TransactionComponent } from './features/transaction-tracking/transaction/transaction.component';
import { BudgetComponent } from './features/manage-budgets/budget/budget.component';
import { GoalComponent } from './features/manage-goals/goal/goal.component';
import { AiComponent } from './features/ai-suggestions/components/ai/ai.component';
import { 
  AuthGuardService as AuthGuard 
} from './core/guards/auth-guard.service';

const routes: Routes = [
  { path: '', component:LandingComponent},
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { 
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'home', component: HomeComponent, canActivate: [AuthGuard]  },
      { path: 'transaction', component: TransactionComponent, canActivate: [AuthGuard]  },
      { path: 'budget', component: BudgetComponent, canActivate: [AuthGuard]  },
      { path: 'goal', component: GoalComponent, canActivate: [AuthGuard]  },
      { path: 'ai', component: AiComponent, canActivate: [AuthGuard]  },
      { path: '**', component:PageNotFoundComponent  }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
