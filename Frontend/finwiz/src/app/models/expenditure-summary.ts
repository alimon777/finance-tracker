interface ExpenditureDetail {
    DEPOSIT: { [key: string]: number };
    WITHDRAW: { [key: string]: number };
    withdrawTotal: number;
}
export interface ExpenditureSummaryDTO {
  weeklyExpenditure: ExpenditureDetail;
  monthlyExpenditure: ExpenditureDetail;
  yearlyExpenditure: ExpenditureDetail;
}