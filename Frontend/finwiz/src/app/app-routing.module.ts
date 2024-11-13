import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { HomeComponent } from './pages/home/home.component';
import { LayoutComponent } from './pages/layout/layout.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { LandingComponent } from './pages/landing/landing.component';
import { TransactionComponent } from './pages/transaction/transaction.component';
import { BudgetComponent } from './pages/budget/budget.component';
import { GoalComponent } from './pages/goal/goal.component';
import { AiComponent } from './pages/ai/ai.component';
import { 
  AuthGuardService as AuthGuard 
} from './service/auth/auth-guard.service';

const routes: Routes = [
  // { path: '', redirectTo: '/home', pathMatch: 'full' },
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
      // { path: '**', component:PageNotFoundComponent  }
    ]
  },
  // Wildcard route for a 404 page redirection
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
