import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { StorageService } from 'src/app/core/services/storage/storage.service'
import { SnackbarService } from 'src/app/core/services/snackbar/snackbar.service';
import { TransactionService } from './../transaction.service';
import { Account } from 'src/app/features/transaction-tracking/models/account.model';
import { Transaction } from 'src/app/features/transaction-tracking/models/transaction.model';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})

export class TransactionComponent implements OnInit {

  accounts: Account[] = [];
  transactions: Transaction[] = [];
  isAddAccountVisible: boolean = false;

  constructor(
    private transactionService: TransactionService,
    private snackbarService: SnackbarService,
  ) {
  }

  ngOnInit(): void {
    this.loadAccounts();
    this.loadTransactions();
  }

  // Existing methods remain the same
  getTotalBalance() {
    return this.accounts.reduce((acc, account) => acc + account.accountBalance, 0);
  }

  loadAccounts(): void {
    this.transactionService.getAllAccounts().subscribe({
      next: (response) => {
        this.accounts = response.data || [];
      },
      error: (error: HttpErrorResponse) => {
        //console.warn(error.message)
      }
    });
  }

  loadTransactions(): void {
    this.transactionService.getTransactions().subscribe({
      next: (response: Transaction[]) => {
        this.transactions = response;
      },
      error: (error: HttpErrorResponse) => {
        this.snackbarService.show(error.message);
      }
    });
  }

  onDeleteAccount(accountId: number): void {
    this.transactionService.deleteAccount(accountId).subscribe({
      next: () => {
        this.accounts = this.accounts.filter(account => account.id !== accountId);
        this.snackbarService.show("Successfully deleted account");
        this.loadAccounts();
        this.loadTransactions();
      },
      error: (error: HttpErrorResponse) => {
        this.snackbarService.show(error.message);
      }
    });
  }

  openAddAccountModal(): void {
    this.isAddAccountVisible = true;
  }

  onAddAccountModalClose(): void {
    this.isAddAccountVisible = false;
  }

  onNewAccountAdded(): void {
    this.snackbarService.show("Account added successfully");
    this.loadAccounts();
    this.loadTransactions();
  }

  onNewTransactionAdded(): void {
    this.snackbarService.show("Account added successfully");
    this.loadAccounts();
    this.loadTransactions();
  }
}