import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccountService } from 'src/app/shared/services/account/account.service';
import { TransactionService } from 'src/app/shared/services/transaction/transaction.service';
import { Account } from 'src/app/shared/models/account';
import { Transaction } from 'src/app/shared/models/transaction';
import { HttpErrorResponse } from '@angular/common/http';
import { StorageService } from 'src/app/core/services/storage/storage.service'
import { SnackbarService } from 'src/app/core/services/snackbar/snackbar.service';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {
  accounts: Account[] = [];
  transactions: Transaction[] = [];
  userId: number = 0;
  isAddAccountVisible:boolean = false;

  constructor(
    private accountService: AccountService,
    private transactionService: TransactionService,
    private storageService: StorageService,
    private snackbarService: SnackbarService
  ) {
  }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
    this.loadAccounts();
    this.loadTransactions();
  }

  // Existing methods remain the same
  getTotalBalance() {
    return this.accounts.reduce((acc, account) => acc + account.accountBalance, 0);
  }

  loadAccounts(): void {
    this.accountService.getAllAccounts(this.userId).subscribe({
      next: (response) => {
        this.accounts = response.data || [];
      },
      error: (error: HttpErrorResponse) => {
        //console.warn(error.message)
        //this.snackbarService.show(error.message);
      }
    });
  }

  loadTransactions(): void {
    this.transactionService.getTransactions(this.userId).subscribe({
      next: (response: Transaction[]) => {
        this.transactions = response;
        // this.filteredTransactions = this.sortTransactionsByDate(this.transactions);
      },
      error: (error: HttpErrorResponse) => {
        this.snackbarService.show(error.message);
      }
    });
  }

  onDeleteAccount(accountId: number): void {
    this.accountService.deleteAccount(accountId).subscribe({
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

  sortTransactionsByDate(transactions: Transaction[]): Transaction[] {
    return transactions.sort((a, b) => {
      const dateA = new Date(a.transactionDate);
      const dateB = new Date(b.transactionDate);
      return dateB.getTime() - dateA.getTime();
    });
  }

  openAddAccountModal(): void {
    this.isAddAccountVisible = true;
  }

  onAddAccountModalClose(): void {
    this.isAddAccountVisible = false;
  }

  onNewAccountAdded(): void {
    this.loadAccounts();
    this.loadTransactions();
  }

  onNewTransactionAdded(): void {
    this.loadAccounts();
    this.loadTransactions();
  }
}