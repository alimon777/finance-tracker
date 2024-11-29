import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionComponent } from './transaction/transaction.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TransactionTableComponent } from './transaction-table/transaction-table.component';

@NgModule({
  declarations: [
    TransactionComponent,
    TransactionTableComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ]
})
export class TransactionTrackingModule { }
