import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StorageService } from 'src/app/core/services/storage/storage.service';
import { Account } from 'src/app/features/transaction-tracking/models/account.model';
import { TransactionService } from './../transaction.service';

@Component({
  selector: 'app-add-account',
  templateUrl: './add-account.component.html',
  styleUrls: ['./add-account.component.css']
})

export class AddAccountComponent implements OnInit {

  @Output() close = new EventEmitter();
  @Output() accountAdded = new EventEmitter();

  accountForm!: FormGroup;
  isVisible: boolean = true;
  userId: number = 0;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private storageService: StorageService,
  ) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
    this.accountForm.valueChanges.subscribe(() => {
      this.errorMessage = '';
    });
  }

  private initializeForm(): void {
    this.accountForm = this.fb.group({
      bankName: ['', [Validators.required, Validators.minLength(2)]],
      accountNumber: ['', [Validators.required, Validators.pattern('^[0-9]{9,18}$')]],
      accountBalance: [null, [Validators.required, Validators.min(500)]]
    });
  }

  onSubmitAccount(): void {
    if (this.accountForm.valid) {
      const newAccount: Account = {
        ...this.accountForm.value,
        userId: this.userId,
        bankName: this.accountForm.value.bankName.toUpperCase()
      };

      this.transactionService.addAccount(newAccount).subscribe({
        next: (response) => {
          if (response.data) {
            this.accountForm.reset();
            this.accountAdded.emit();
            this.onCancel();
          }
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage = error.message;
        }
      });
    } else {
      this.markFormGroupTouched(this.accountForm);
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

  get bankName() { return this.accountForm.get('bankName'); }
  get accountNumber() { return this.accountForm.get('accountNumber'); }
  get accountBalance() { return this.accountForm.get('accountBalance'); }

  onCancel(): void {
    this.isVisible = false;
    this.close.emit();
  }
}
