export class Transaction {
    id!: number; // Add this field for the transaction ID
    accountNumber!: string;
    description!: string;
    amount!: number;
    transactionType!: string; // This should be an enum or string depending on your design
    categoryType!: string ; // This should also match your backend enum
    userId!: number; // Include userId for the transaction
    transactionDate!: Date; // Include this field if you want to manage the date of the transaction
}
