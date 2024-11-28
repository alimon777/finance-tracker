import { Component, OnInit } from '@angular/core';
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
  accountForm!: FormGroup;
  transactionForm!: FormGroup;
  accounts: Account[] = [];
  transactions: Transaction[] = [];
  userId: number = 0;

  constructor(
    private fb: FormBuilder,
    private accountService: AccountService,
    private transactionService: TransactionService,
    private storageService: StorageService,
    private snackbarService: SnackbarService
  ) {
    this.initializeForms();
  }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
    this.loadAccounts();
    this.loadTransactions();
  }

  private initializeForms(): void {
    // Initialize Account Form
    this.accountForm = this.fb.group({
      bankName: ['', [Validators.required, Validators.minLength(2)]],
      accountNumber: ['', [Validators.required, Validators.pattern('^[0-9]{9,18}$')]],
      accountBalance: [null, [Validators.required, Validators.min(500)]]
    });

    // Initialize Transaction Form
    this.transactionForm = this.fb.group({
      accountNumber: ['', Validators.required],
      amount: [null, [Validators.required, Validators.min(1)]],
      transactionType: ['', Validators.required],
      categoryType: ['', Validators.required],
      description: ['', [Validators.required, Validators.minLength(0)]]
    });

    // Subscribe to transaction type changes
    this.transactionForm.get('transactionType')?.valueChanges.subscribe(type => {
      const categoryControl = this.transactionForm.get('categoryType');
      if (type === 'DEPOSIT') {
        categoryControl?.setValue('INCOME');
        categoryControl?.disable();
      } else if (type === 'WITHDRAW') {
        categoryControl?.enable();
        categoryControl?.setValue('');
      }
    });
  }

  // Form Error Getters for Account Form
  get bankName() { return this.accountForm.get('bankName'); }
  get accountNumber() { return this.accountForm.get('accountNumber'); }
  get accountBalance() { return this.accountForm.get('accountBalance'); }

  // Form Error Getters for Transaction Form
  get transAmount() { return this.transactionForm.get('amount'); }
  get transAccountNumber() { return this.transactionForm.get('accountNumber'); }
  get transType() { return this.transactionForm.get('transactionType'); }
  get transCategory() { return this.transactionForm.get('categoryType'); }
  get transDescription() { return this.transactionForm.get('description'); }

  onSubmitAccount(): void {
    if (this.accountForm.valid) {
      const newAccount: Account = {
        ...this.accountForm.value,
        userId: this.userId,
        bankName: this.accountForm.value.bankName.toUpperCase()
      };

      this.accountService.addAccount(newAccount).subscribe({
        next: (response) => {
          if (response.data) {
            this.accounts.push(response.data);
            this.snackbarService.show("Successfully added account");
            this.loadTransactions();
            this.accountForm.reset();
          }
        },
        error: (error: HttpErrorResponse) => {
          this.snackbarService.show(error.message);
        }
      });
    } else {
      this.markFormGroupTouched(this.accountForm);
    }
  }

  onSubmitTransaction(): void {
    if (this.transactionForm.valid) {
      const newTransaction: Transaction = {
        ...this.transactionForm.getRawValue(),
        id: 0,
        userId: this.userId,
        transactionDate: new Date()
      };

      this.transactionService.addTransaction(newTransaction).subscribe({
        next: () => {
          this.snackbarService.show("Transaction added successfully");
          this.loadTransactions();
          this.loadAccounts();
          this.transactionForm.reset();
        },
        error: (error: HttpErrorResponse) => {
          this.snackbarService.show(error.message);
        }
      });
    } else {
      this.markFormGroupTouched(this.transactionForm);
    }
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
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
}