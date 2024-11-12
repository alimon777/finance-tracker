import { Component, OnInit } from '@angular/core';
import { AccountService } from 'src/app/service/account/account.service';
import { Account } from 'src/app/models/account';
import { HttpErrorResponse } from '@angular/common/http';
import { Transaction } from 'src/app/models/transaction';
import { TransactionService } from 'src/app/service/transaction/transaction.service';
import { StorageService } from 'src/app/service/storage/storage.service';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {
  newTransaction: Transaction = {
    id: 0, // Set a default value or null if not necessary
    amount: 0,
    userId: 0, // Initialized later
    description: '',
    accountNumber: '',
    transactionDate: new Date(), // Set current date or null if not necessary
    transactionType: '',
    categoryType: '',
  };

  transactions: Transaction[] = [];

  // Account details
  accounts: Account[] = []; // Array to hold accounts
  newAccount: Account = new Account(); // Model for creating a new account

  // User ID variable
  userId: number = 0;

  constructor(
    private accountService: AccountService,
    private transactionService: TransactionService,
    private storageService: StorageService
  ) { }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId(); 
    this.loadAccounts(); // Load accounts on initialization
    this.loadTransactions();
  }
  getTotalBalance() {
    return this.accounts.reduce((acc, account) => acc + account.accountBalance, 0);
  }

  loadAccounts(): void {
    if (this.userId) {
      this.accountService.getAllAccounts(this.userId).subscribe({
        next: (response) => {
          this.accounts = response.data || []; // Load accounts for the user, default to an empty array if null
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading accounts:', error.message);
        }
      });
    } else {
      console.warn('User ID not found in local storage.'); // Warn if userId is not found
    }
  }

  onSubmitAccount(): void {
    // Set userId in the newAccount object
    this.newAccount.userId = this.userId;
    this.newAccount.bankName = this.newAccount.bankName.toUpperCase();
    this.accountService.addAccount(this.newAccount).subscribe({
      next: (response) => {
        if (response.data) { // Ensure response.data is not null
          this.accounts.push(response.data); // Add the created account to the list
        }
        this.loadAccounts();
        this.loadTransactions();
        this.newAccount = new Account(); // Reset the form
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error adding account:', error.message);
      }
    });
  }

  onDeleteAccount(accountId: number): void {
    this.accountService.deleteAccount(accountId).subscribe({
      next: () => {
        this.accounts = this.accounts.filter(account => account.id !== accountId);
        this.loadAccounts(); // Remove the deleted account from the list
        this.loadTransactions();
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error deleting account:', error.message);
      }
    });
  }

  loadTransactions(): void {
    if (this.userId) {
      this.transactionService.getTransactions(this.userId).subscribe({
        next: (response: Transaction[]) => { // Specify the response type here
          this.transactions = response; // Ensure the response structure matches
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading transactions:', error.message);
        },
      });
    } else {
      console.warn('User ID not found in local storage.');
    }
  }

  onSubmitTransaction(): void {
    // Optionally set transactionDate to current date
    this.newTransaction.transactionDate = new Date();
    this.newTransaction.userId=this.userId;    
    this.transactionService.addTransaction(this.newTransaction).subscribe({
      next: (response) => {
        console.log('Transaction added', response);
        this.loadTransactions(); // Reload transactions after adding
        this.loadAccounts();
        this.resetTransactionForm(); // Reset form after submission
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error adding transaction', error.message);
      },
    });

  }

  resetTransactionForm(): void {
    this.newTransaction = {
      id: 0, // Resetting id
      amount: 0,
      userId: this.userId,
      description: '',
      accountNumber: '',
      transactionDate: new Date(), // Resetting to current date
      transactionType: '',
      categoryType: '',
    };
  }
  onTransactionTypeChange() {
    // Reset categoryType when transaction type changes
    this.newTransaction.categoryType = this.newTransaction.transactionType === 'DEPOSIT' ? 'INCOME' : '';
}

}