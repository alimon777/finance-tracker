export interface Budget {
    id: number;
    userId: number;
    username: string;
    email:string;
    budgetStartDate: Date;
    budgetEndDate: Date;
    food: number;
    housing: number;
    transportation: number;
    entertainment: number;
    aiGenerated: boolean;
    total:number;
  }
  