import { Injectable } from '@angular/core';
import { GoogleGenerativeAI } from '@google/generative-ai';
import { HttpClient } from '@angular/common/http';
import { Budget } from 'src/app/models/budget';
import { ExpenditureSummaryDTO } from 'src/app/models/expenditure-summary';
import { Goal } from 'src/app/models/goal';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AiService {
  private genAi: GoogleGenerativeAI;
  private model: any;
  private aiBudget: Budget = new Budget();
  
  // Debug subjects
  private debugState = new BehaviorSubject<string>('idle');
  public debugState$ = this.debugState.asObservable();

  constructor(
    private http: HttpClient
  ) {
    try {
      this.genAi = new GoogleGenerativeAI("AIzaSyChX9wjdsFCNuWunB_7PhMPU1UxJDDJmOI");
      this.model = this.genAi.getGenerativeModel({ model: "gemini-1.5-flash" });
      console.debug('[AiService] Initialized successfully');
    } catch (error) {
      console.error('[AiService] Initialization failed:', error);
      throw new Error('AI Service initialization failed');
    }
  }

  async generateBudgetSuggestion(expenditureSummary: ExpenditureSummaryDTO, goals: Goal[]): Promise<string> {
    this.debugState.next('generating');
    console.debug('[generateBudgetSuggestion] Starting with:', { expenditureSummary, goals });

    if (!expenditureSummary || !goals) {
      console.error('[generateBudgetSuggestion] Invalid input data');
      this.debugState.next('error');
      throw new Error('Invalid input data for budget suggestion');
    }

    try {
      const prompt = this.createBudgetPrompt(expenditureSummary, goals);
      console.debug('[generateBudgetSuggestion] Generated prompt:', prompt);

      const result = await this.model.generateContent(prompt);
      console.debug('[generateBudgetSuggestion] Raw AI response:', result);

      if (!result?.response) {
        throw new Error('Empty response from AI service');
      }

      const text = await result.response.text();
      this.debugState.next('completed');
      return text;

    } catch (error) {
      console.error('[generateBudgetSuggestion] Error:', error);
      this.debugState.next('error');
      throw new Error(`Budget generation failed: ${error}`);
    }
  }

  parseAiGeneratedBudgetResponse(response: string, userId: number): Budget {
    console.debug('[parseAiGeneratedBudgetResponse] Starting parse:', { response, userId });

    if (!response || !userId) {
      console.error('[parseAiGeneratedBudgetResponse] Invalid input');
      throw new Error('Invalid input for budget parsing');
    }

    try {
      // Updated regex patterns to match the new format
      const startDatePattern = /Budget StartDate: <strong class="font-bold">(\d{4}-\d{2}-\d{2})<\/strong>/;
      const amountPattern = /<em>Budget for (\w+):<\/em> ₹(\d+(\.\d{1,2})?)/g;

      const startDateMatch = startDatePattern.exec(response);
      console.debug('[parseAiGeneratedBudgetResponse] Date match:', startDateMatch);

      if (!startDateMatch) {
        throw new Error('Could not parse budget start date');
      }

      const budget = new Budget();
      budget.budgetStartDate = new Date(startDateMatch[1]);
      budget.budgetEndDate = new Date(budget.budgetStartDate.getFullYear(), 
                                    budget.budgetStartDate.getMonth() + 1, 0);
      budget.userId = userId;
      budget.aiGenerated = true;

      let match;
      while ((match = amountPattern.exec(response)) !== null) {
        const category = match[1].toLowerCase();
        const amount = parseFloat(match[2]);
        console.debug(`[parseAiGeneratedBudgetResponse] Parsed ${category}: ₹${amount}`);

        switch (category) {
          case 'food':
            budget.food = amount;
            break;
          case 'entertainment':
            budget.entertainment = amount;
            break;
          case 'housing':
            budget.housing = amount;
            break;
          case 'transportation':
            budget.transportation = amount;
            break;
        }
      }

      if (!budget.food && !budget.entertainment && !budget.housing && !budget.transportation) {
        throw new Error('No budget categories were parsed successfully');
      }

      console.debug('[parseAiGeneratedBudgetResponse] Final budget:', budget);
      return budget;

    } catch (error) {
      console.error('[parseAiGeneratedBudgetResponse] Parse error:', error);
      throw new Error(`Failed to parse AI response: ${error}`);
    }
  }

  private createBudgetPrompt(expenditureSummary: ExpenditureSummaryDTO, goals: Goal[]): string {
    console.debug('[createBudgetPrompt] Creating prompt with:', { expenditureSummary, goals });
    
    const today = new Date().toISOString().split('T')[0];
    
    return `
      Based on the following expenditure summary and financial goals, provide a budget suggestion:

      Expenditure Summary:
      WEEKLY:
      Total Income: ₹${expenditureSummary.weeklyExpenditure.DEPOSIT || 0}
      Total Expenses: ₹${expenditureSummary.weeklyExpenditure.withdrawTotal || 0}
      Food: ₹${expenditureSummary.weeklyExpenditure.WITHDRAW?.['FOOD'] || 0}
      Entertainment: ₹${expenditureSummary.weeklyExpenditure.WITHDRAW?.['ENTERTAINMENT'] || 0}
      Housing: ₹${expenditureSummary.weeklyExpenditure.WITHDRAW?.['HOUSING'] || 0}
      Transportation: ₹${expenditureSummary.weeklyExpenditure.WITHDRAW?.['TRANSPORTATION'] || 0}

      MONTHLY:
      Total Income: ₹${expenditureSummary.monthlyExpenditure.DEPOSIT || 0}
      Total Expenses: ₹${expenditureSummary.monthlyExpenditure.withdrawTotal || 0}
      Food: ₹${expenditureSummary.monthlyExpenditure.WITHDRAW?.['FOOD'] || 0}
      Entertainment: ₹${expenditureSummary.monthlyExpenditure.WITHDRAW?.['ENTERTAINMENT'] || 0}
      Housing: ₹${expenditureSummary.monthlyExpenditure.WITHDRAW?.['HOUSING'] || 0}
      Transportation: ₹${expenditureSummary.monthlyExpenditure.WITHDRAW?.['TRANSPORTATION'] || 0}

      FINANCIAL GOALS:
      ${goals.map(goal => 
        `Goal: ${goal.goalName}, Amount: ₹${goal.value}, Duration: ${goal.durationInMonths} months`
      ).join('\n')}

      Please provide a budget plan in exactly this format:

      <div class="flex justify-center items-center space-x-4">
      <p>Budget StartDate: <strong class="font-bold">${today}</strong></p>
      <p>Budget EndDate: <strong class="font-bold">[CALCULATED_END_DATE]</strong></p>
      </div>
      <br />
      <ul class="grid-cols-2 gap-4 flex justify-center items-center space-x-4">
      <li><em>Budget for Food:</em> ₹[AMOUNT]</li>
      <li><em>Budget for Entertainment:</em> ₹[AMOUNT]</li>
      <li><em>Budget for Housing:</em> ₹[AMOUNT]</li>
      <li><em>Budget for Transportation:</em> ₹[AMOUNT]</li>
      </ul>
      <br />
      [BRIEF_EXPLANATION]
    `;
  }
}