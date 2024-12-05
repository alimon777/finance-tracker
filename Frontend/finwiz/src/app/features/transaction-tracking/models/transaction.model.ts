export interface Transaction {
    id: number; 
    accountNumber: string;
    description: string;
    amount: number;
    transactionType: string; 
    categoryType: string ; 
    userId: number; 
    transactionDate: Date; 
}
