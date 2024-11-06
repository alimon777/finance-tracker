export class Budget {
    id!: number;
    userId!: number;
    budgetStartDate!: Date;
    budgetEndDate!: Date;
    food!: number;
    housing!: number;
    transportation!: number;
    entertainment!: number;
    aiGenerated!: boolean;
    isExceeded!: boolean;
  }
  