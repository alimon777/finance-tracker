export interface Goal {
    userId?:number;
    goalName: string;
    value: number;
    description?: string;
    durationInMonths: number;
    startDate: Date;
  }
  
