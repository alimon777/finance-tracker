import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LandingComponent } from './components/landing/landing.component';
import { LayoutComponent } from './components/layout/layout.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';

@NgModule({
  declarations: [
    LandingComponent,
    PageNotFoundComponent,
  ],
  imports: [
    CommonModule,
    LayoutComponent,
  ]
})
export class SharedModule { }
