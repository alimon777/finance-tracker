import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IncomeDepositDTO } from 'src/app/features/dashboard/income-deposit.model';
import { environment } from 'src/environments/environment';

interface ExpenditureDetail {
  DEPOSIT: { [key: string]: number };
  WITHDRAW: { [key: string]: number };
  withdrawTotal: number;
}

interface ExpenditureSummaryDTO {
  weeklyExpenditure: ExpenditureDetail;
  monthlyExpenditure: ExpenditureDetail;
  yearlyExpenditure: ExpenditureDetail;
}

@Injectable()

export class DashboardService {
  private apiUrl = environment.apiBaseUrl + '/api/transactions/summary';

  constructor(private http: HttpClient) { }

  getExpenditureSummary(): Observable<ExpenditureSummaryDTO> {
    return this.http.get<ExpenditureSummaryDTO>(this.apiUrl);
  }

  getIncomeDepositSummary(periodLabel: string): Observable<IncomeDepositDTO[]> {
    return this.http.get<IncomeDepositDTO[]>(`${this.apiUrl}/${periodLabel}`);
  }
}
