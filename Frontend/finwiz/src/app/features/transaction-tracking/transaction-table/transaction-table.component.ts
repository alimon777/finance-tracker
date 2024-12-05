import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Account } from 'src/app/features/transaction-tracking/models/account.model';
import { Transaction } from 'src/app/features/transaction-tracking/models/transaction.model';

@Component({
  selector: 'app-transaction-table',
  templateUrl: './transaction-table.component.html',
  styleUrls: ['./transaction-table.component.css']
})

export class TransactionTableComponent implements OnInit, OnChanges {

  @Input() transactions: Transaction[] = [];
  @Input() accounts: Account[] = [];
  @Output() transactionAdded = new EventEmitter();

  filteredTransactions: Transaction[] = [];
  isFilterApplied = false;
  filterForm!: FormGroup;
  isFilterFormVisible = false;
  currentSortColumn: string = 'transactionDate';
  currentSortDirection: 'asc' | 'desc' = 'desc';
  isAddTransactionVisible: boolean = false;

  constructor(
    private fb: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.isFilterApplied = false;
    this.filteredTransactions = this.sortTransactions(this.transactions);
    this.initFilterForm();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['transactions']) {
      if (this.isFilterApplied)
        this.applyFilters();
      else {
        this.filteredTransactions = this.sortTransactions(this.transactions);
        this.initFilterForm();
      }
    }
  }

  toggleFilterForm() {
    this.isFilterFormVisible = !this.isFilterFormVisible;
  }

  initFilterForm() {
    this.filterForm = this.fb.group({
      dateFrom: [null],
      dateTo: [null],
      accountNumber: [''],
      transactionType: [''],
      categoryType: [''],
      minAmount: [null],
      maxAmount: [null]
    });

    // Apply filter whenever form changes
    this.filterForm.valueChanges.subscribe(() => {
      this.applyFilters();
    });
  }

  applyFilters() {
    this.isFilterApplied = true;
    let result = [...this.transactions];

    const filter = this.filterForm.value;

    if (filter.dateFrom) {
      result = result.filter(t => new Date(t.transactionDate) >= new Date(filter.dateFrom));
    }
    if (filter.dateTo) {
      result = result.filter(t => new Date(t.transactionDate) <= new Date(filter.dateTo));
    }
    if (filter.accountNumber) {
      result = result.filter(t => t.accountNumber.includes(filter.accountNumber));
    }
    if (filter.transactionType) {
      result = result.filter(t => t.transactionType === filter.transactionType);
    }
    if (filter.categoryType) {
      result = result.filter(t => t.categoryType === filter.categoryType);
    }
    if (filter.minAmount !== null) {
      result = result.filter(t => t.amount >= filter.minAmount);
    }
    if (filter.maxAmount !== null) {
      result = result.filter(t => t.amount <= filter.maxAmount);
    }
    this.filteredTransactions = this.sortTransactions(result);
  }

  sortTransactions(transactions: Transaction[]): Transaction[] {
    return transactions.sort((a, b) => {
      let comparison = 0;

      switch (this.currentSortColumn) {
        case 'transactionDate':
          comparison = new Date(a.transactionDate).getTime() - new Date(b.transactionDate).getTime();
          break;
        case 'amount':
          comparison = a.amount - b.amount;
          break;
        case 'categoryType':
          comparison = a.categoryType.localeCompare(b.categoryType);
          break;
        case 'transactionType':
          comparison = a.transactionType.localeCompare(b.transactionType);
          break;
      }

      return this.currentSortDirection === 'asc' ? comparison : -comparison;
    });
  }

  onSortColumn(column: string) {
    // If same column is clicked, toggle sort direction
    if (this.currentSortColumn === column) {
      this.currentSortDirection = this.currentSortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      // New column, default to descending
      this.currentSortColumn = column;
      this.currentSortDirection = 'desc';
    }
    // Re-apply sorting to current filtered results
    this.applyFilters();
  }

  // Reset filters
  resetFilters() {
    this.initFilterForm();
    this.isFilterApplied = false;
    this.filteredTransactions = [...this.transactions];
  }

  openAddTransactionModal(): void {
    this.isAddTransactionVisible = true;
  }

  onAddTransactionModalClose(): void {
    this.isAddTransactionVisible = false;
  }
}
