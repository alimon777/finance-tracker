import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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

@Injectable({
  providedIn: 'root'
})
export class ExpenditureService {
  private apiUrl = ' http://localhost:8000/test/transactions/expenditure-summary'; // Replace with your actual backend URL

  constructor(private http: HttpClient) {}

  getExpenditureSummary(userId: number): Observable<ExpenditureSummaryDTO> {
    return this.http.get<ExpenditureSummaryDTO>(`${this.apiUrl}/${userId}`);
  }
}
