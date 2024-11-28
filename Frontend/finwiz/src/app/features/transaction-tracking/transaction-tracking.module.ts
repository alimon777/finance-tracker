import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionComponent } from './transaction/transaction.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    TransactionComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ]
})
export class TransactionTrackingModule { }
