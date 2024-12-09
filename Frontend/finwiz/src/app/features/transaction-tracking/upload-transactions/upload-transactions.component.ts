import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Transaction } from '../models/transaction.model';
import * as XLSX from 'xlsx';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Account } from '../models/account.model';
import { StorageService } from 'src/app/core/services/storage/storage.service';
import { TransactionService } from '../transaction.service';

@Component({
  selector: 'app-upload-transactions',
  templateUrl: './upload-transactions.component.html',
  styleUrls: ['./upload-transactions.component.css']
})

export class UploadTransactionsComponent implements OnInit {
  @Input() accounts!: Account[];
  @Output() close = new EventEmitter();
  @Output() transactionAdded = new EventEmitter();

  transactionTotal: number = 0;
  userId: number = 0;
  isVisible = true;
  transactions: Transaction[] = [];
  errorMessage = '';

  transactionForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private storageService: StorageService,
    private transactionService: TransactionService,
  ) {
    this.transactionForm = this.fb.group({
      accountNumber: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.userId = this.storageService.fetchUserId();
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    this.errorMessage = '';

    if (file) {
      this.transactions = [];
      const reader = new FileReader();
      const parseExcelDate = (excelDate: any): Date => {
        const excelEpoch = new Date(1899, 11, 30);
        if (!isNaN(excelDate)) {
          return new Date(excelEpoch.getTime() + excelDate * 86400000);
        }
        return new Date(excelDate);
      };

      reader.onload = (e: any) => {
        try {
          const workbook = XLSX.read(e.target.result, { type: 'binary' });
          const sheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[sheetName];
          const data: any[] = XLSX.utils.sheet_to_json(worksheet);

          this.transactions = data.map(item => ({
            id: 0,
            accountNumber: this.transAccountNumber?.value,
            description: item.Description,
            amount: parseFloat(item.Amount),
            transactionType: item.TransactionType,
            categoryType: item.CategoryType,
            userId: this.userId,
            transactionDate: parseExcelDate(item.Date)
          }));
        } catch (error) {
          this.errorMessage = 'Error parsing Excel file. Please check the file format.';
          this.transactions = [];
        }
      };
      reader.readAsBinaryString(file);
    }
  }

  get transAccountNumber() {
    return this.transactionForm.get('accountNumber');
  }

  onCancel() {
    this.isVisible = false;
    this.close.emit();
    this.transactions = [];
    this.transactionForm.reset();
  }

  onSubmit(): void {
    if (this.transactionForm.valid && this.transactions.length > 0) {
      this.transactions = this.transactions.map(transaction => ({
        ...transaction, 
        accountNumber: this.transAccountNumber?.value,
      }));
      try {
        this.transactionService.addMultipleTransactions(this.transactions).subscribe({
          next: (response) => {
            this.transactionAdded.emit();
            this.onCancel();
          },
          error: (error) => {
            this.errorMessage = 'Failed to submit Transactions. Please try again.';
          }
        });
      } catch (error) {
        this.errorMessage = 'An unexpected error occurred. Please try again.';
      }
    } else {
      this.transactionForm.markAllAsTouched();

      if (this.transactions.length === 0) {
        this.errorMessage = 'Please upload transactions first.';
      }
    }
  }


}
