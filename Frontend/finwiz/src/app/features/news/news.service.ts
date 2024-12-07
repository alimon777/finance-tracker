import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable()

export class NewsService {
  private apiKey = environment.newsApiKey;
  private apiUrl = environment.newsApiUrl;

  constructor(private http: HttpClient) {}

  getFinancialNews(): Observable<any> {
    // Request for financial news filtered by category and country
    return this.http.get(`${this.apiUrl}?category=business&country=us&apiKey=${this.apiKey}`);
  }
}