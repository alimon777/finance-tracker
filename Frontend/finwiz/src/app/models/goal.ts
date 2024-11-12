export interface Goal {
    [x: string]: any;
    userId?:number;
    goalName: string;
    value: number | null;
    description?: string;
    durationInMonths: number | null;
    startDate: Date;
  }
  
