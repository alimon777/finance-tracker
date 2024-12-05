export interface Expenditure {
    deposit: {
      INCOME: number;
    };
    withdraw: {
      ENTERTAINMENT: number;
      TRANSPORTATION: number;
      HOUSING: number;
      FOOD: number;
    };
    withdrawTotal: number;
  }