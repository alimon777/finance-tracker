import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionComponent } from './transaction/transaction.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TransactionTableComponent } from './transaction-table/transaction-table.component';
import { AddAccountComponent } from './add-account/add-account.component';
import { AddTransactionComponent } from './add-transaction/add-transaction.component';
import { TransactionService } from './transaction.service';

@NgModule({
  declarations: [
    TransactionComponent,
    TransactionTableComponent,
    AddAccountComponent,
    AddTransactionComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
  providers: [
    TransactionService
  ]
})

export class TransactionTrackingModule { }
