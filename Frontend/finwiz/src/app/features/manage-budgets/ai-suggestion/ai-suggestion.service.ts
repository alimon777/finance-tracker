import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AiSuggestion } from 'src/app/shared/models/budget';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AiSuggestionService {

  private apiUrl = environment.apiBaseUrl + '/api/gemini/suggestion';


  constructor(private http: HttpClient) { }

  getAiSuggestion(userId: number): Observable<AiSuggestion> {
    return this.http.get<AiSuggestion>(`${this.apiUrl}/${userId}`);
  }
}
