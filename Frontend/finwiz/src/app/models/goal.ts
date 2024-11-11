export interface Goal {
    [x: string]: any;
    userId?:number;
    goalName: string;
    value: number;
    description?: string;
    durationInMonths: number;
    startDate: Date;
  }
  
