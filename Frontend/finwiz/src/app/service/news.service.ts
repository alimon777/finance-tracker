import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  private apiKey = 'd3bbc5fd9aa14a4ba28a471271f6b680'; // Replace with your News API key
  private apiUrl = 'https://newsapi.org/v2/top-headlines';

  constructor(private http: HttpClient) {}

  getFinancialNews(): Observable<any> {
    // Request for financial news, you can filter by category and country
    return this.http.get(`${this.apiUrl}?category=business&country=us&apiKey=${this.apiKey}`);
  }
}