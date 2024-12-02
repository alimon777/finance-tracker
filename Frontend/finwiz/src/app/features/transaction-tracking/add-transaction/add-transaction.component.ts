import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StorageService } from 'src/app/core/services/storage/storage.service';
import { Account } from 'src/app/shared/models/account';
import { Transaction } from 'src/app/shared/models/transaction';
import { TransactionService } from 'src/app/shared/services/transaction/transaction.service';

@Component({
  selector: 'app-add-transaction',
  templateUrl: './add-transaction.component.html',
  styleUrls: ['./add-transaction.component.css']
})
export class AddTransactionComponent implements OnInit {
  @Input() accounts!: Account[];
  @Output() close = new EventEmitter();
  @Output() transactionAdded = new EventEmitter();

  transactionForm!: FormGroup;
  isVisible: boolean = true;
  userId: number = 0;
  errorMessage: string = '';
  currentDate: string = new Date().toISOString().split('T')[0];

  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private storageService: StorageService,
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
    this.transactionForm.valueChanges.subscribe(() => {
      this.errorMessage = '';
    });
  }

  private initializeForm(): void {
    // Initialize Transaction Form
    this.transactionForm = this.fb.group({
      accountNumber: ['', Validators.required],
      amount: [null, [Validators.required, Validators.min(1)]],
      transactionDate: [new Date().toISOString().split('T')[0], Validators.required],
      transactionType: ['', Validators.required],
      categoryType: ['', Validators.required],
      description: ['', [Validators.required, Validators.minLength(0)]]
    });

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

  get transAmount() { return this.transactionForm.get('amount'); }
  get transAccountNumber() { return this.transactionForm.get('accountNumber'); }
  get transType() { return this.transactionForm.get('transactionType'); }
  get transDate() { return this.transactionForm.get('transactionDate'); }
  get transCategory() { return this.transactionForm.get('categoryType'); }
  get transDescription() { return this.transactionForm.get('description'); }

  onSubmitTransaction(): void {
    if (this.transactionForm.valid) {
      const newTransaction: Transaction = {
        ...this.transactionForm.getRawValue(),
        id: 0,
        userId: this.userId,
      };

      this.transactionService.addTransaction(newTransaction).subscribe({
        next: () => {
          this.transactionAdded.emit();
          this.onCancel();
          this.transactionForm.reset();
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message;
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

  onCancel(): void {
    this.isVisible = false;
    this.close.emit();
  }
}
