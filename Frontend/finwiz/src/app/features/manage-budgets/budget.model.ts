export interface Budget {
  id: number;
  userId: number;
  budgetStartDate: Date;
  budgetEndDate: Date;
  food: number;
  housing: number;
  transportation: number;
  entertainment: number;
  aiGenerated: boolean;
  total: number;
}

export interface AiSuggestion {
  textContent: string;
  budget: Budget;
}