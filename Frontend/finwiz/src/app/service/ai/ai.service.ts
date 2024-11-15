import { Injectable } from '@angular/core';
import { GoogleGenerativeAI } from '@google/generative-ai';
import { HttpClient } from '@angular/common/http';
import { Budget } from 'src/app/models/budget';
import { ExpenditureSummaryDTO } from 'src/app/models/expenditure-summary';
import { Goal } from 'src/app/models/goal';

@Injectable({
  providedIn: 'root'
})
export class AiService {
  private genAi: GoogleGenerativeAI;
  private model: any;
  private aiBudget: Budget = new Budget();

  constructor(
    private http: HttpClient
  ) {
    this.genAi = new GoogleGenerativeAI("AIzaSyChX9wjdsFCNuWunB_7PhMPU1UxJDDJmOI");
    this.model = this.genAi.getGenerativeModel({ model: "gemini-1.5-flash" });
  }

  async generateBudgetSuggestion(expenditureSummary: ExpenditureSummaryDTO, goals: Goal[]): Promise<string> {
    const prompt = this.createBudgetPrompt(expenditureSummary, goals);

    try {
      const result = await this.model.generateContent(prompt);

      // Ensure result.response exists and has text
      if (result && result.response && typeof result.response.text === 'function') {
        return result.response.text();
      } else {
        throw new Error("Unexpected response format from AI service.");
      }
    } catch (error) {
      console.error("Error generating budget:", error);
      throw new Error("Could not generate budget suggestion. Please try again later.");
    }
  }

  // Helper function to create a detailed prompt based on transaction data
  createBudgetPrompt(expenditureSummary: ExpenditureSummaryDTO, goals: Goal[]): string {
    return `
      Based on the following expenditure summary of weekly, monthly and yearly, and his Financial goals please provide a budget suggestion:

      Expenditure Summary:
      WEEKLY:
      Total Income: ₹${expenditureSummary.weeklyExpenditure.DEPOSIT}
      Total Expenses: ₹${expenditureSummary.weeklyExpenditure.withdrawTotal}
      Food Expenses: ₹${expenditureSummary.weeklyExpenditure.WITHDRAW?.['FOOD']}
      Entertainment Expenses: ₹${expenditureSummary.weeklyExpenditure.WITHDRAW?.['ENTERTAINMENT']}
      Housing Expenses: ₹${expenditureSummary.weeklyExpenditure.WITHDRAW?.['HOUSING']}
      Transportation Expenses: ₹${expenditureSummary.weeklyExpenditure.WITHDRAW?.['TRANSPORTATION']}

      MONTHLY:
      Total Income: ₹${expenditureSummary.monthlyExpenditure.DEPOSIT}
      Total Expenses: ₹${expenditureSummary.monthlyExpenditure.withdrawTotal}
      Food Expenses: ₹${expenditureSummary.monthlyExpenditure.WITHDRAW?.['FOOD']}
      Entertainment Expenses: ₹${expenditureSummary.monthlyExpenditure.WITHDRAW?.['ENTERTAINMENT']}
      Housing Expenses: ₹${expenditureSummary.monthlyExpenditure.WITHDRAW?.['HOUSING']}
      Transportation Expenses: ₹${expenditureSummary.monthlyExpenditure.WITHDRAW?.['TRANSPORTATION']}

      YEARLY:
      Total Income: ₹${expenditureSummary.yearlyExpenditure.DEPOSIT}
      Total Expenses: ₹${expenditureSummary.yearlyExpenditure.withdrawTotal}
      Food Expenses: ₹${expenditureSummary.yearlyExpenditure.WITHDRAW?.['FOOD']}
      Entertainment Expenses: ₹${expenditureSummary.yearlyExpenditure.WITHDRAW?.['ENTERTAINMENT']}
      Housing Expenses: ₹${expenditureSummary.yearlyExpenditure.WITHDRAW?.['HOUSING']}
      Transportation Expenses: ₹${expenditureSummary.yearlyExpenditure.WITHDRAW?.['TRANSPORTATION']}

      FINANCIAL GOALS
      ${goals.map(t => `Goal Name: ${t.goalName}, Amount: ₹${t.value}, Duration in Months: ₹${t.durationInMonths}, Description: ${t.description}, StartDate: ${t.startDate}`).join('\n')}
  
      Based on these expenses, please suggest a budget plan using the following guidelines:
  
      - Please suggest new budget amounts for **Food**, **Entertainment**, **Housing**, and **Transportation** based on the current spending.
      - You can suggest an overall budget structure for each category (Food, Entertainment, Housing, Transportation) with suggested amounts based on the **current spending** and your expertise in budget planning.
      - Use <strong> tags for important details like Total Income, Total Expenses, etc.
      - Use <ul><li> tags to list out suggested budget categories.
      - Use <em> for emphasis on important categories, such as food.
      - Ensure that the sum of all budget categories (Food, Entertainment, Housing, Transportation) does not exceed the total expenses or income.
      - Ensure that the Budget start is greater than or equaul to today's date
      Format the response like this:
  
        <div class="flex justify-center items-center space-x-4">
        <p>Budget StartDate: <strong class="font-bold">[YYYY-MM-DD]</strong></p>
        <p>Budget EndDate: <strong class="font-bold">[YYYY-MM-DD]</strong></p>
        </div>
        <br />
        <ul class=" grid-cols-2 gap-4 flex justify-center items-center space-x-4">
        <li><em>Budget for Food:</em> ₹[amount]</li>
        <li><em>Budget for Entertainment:</em> ₹[amount]</li>
        <li><em>Budget for Housing:</em> ₹[amount]</li>
        <li><em>Budget for Transportation:</em> ₹[amount]</li>
        </ul>
        <br />

      [Brief one small paragraph explanation of how the suggested budget amounts were calculated based on the current spending.]
    `;
  }

  parseAiGeneratedBudgetResponse(response: any, userId: number): Budget {
    if (!response) {
      throw new Error("AI response format is incorrect.");
    }

    // Regular expressions to match the date ranges and budget amounts
    const datePattern = /Budget StartDate: <strong class="font-bold">([^<]+)<\/strong>/;
    const amountPattern = /Budget for (\w+):<\/em> \$(\d+(\.\d{1,2})?)/g;

    // Extract the budget start and end dates
    const dateMatch = datePattern.exec(response);
    this.aiBudget.budgetStartDate = dateMatch ? new Date(dateMatch[1]) : new Date();
    this.aiBudget.budgetEndDate = new Date(this.aiBudget.budgetStartDate.getFullYear(), this.aiBudget.budgetStartDate.getMonth() + 1, 0);

    // Extract the budget amounts
    let match;
    while ((match = amountPattern.exec(response)) !== null) {
      const category = match[1].toLowerCase();
      const amount = parseFloat(match[2]);

      switch (category) {
        case 'food':
          this.aiBudget.food = amount;
          break;
        case 'entertainment':
          this.aiBudget.entertainment = amount;
          break;
        case 'housing':
          this.aiBudget.housing = amount;
          break;
        case 'transportation':
          this.aiBudget.transportation = amount;
          break;
      }
    }

    this.aiBudget.aiGenerated = true;
    this.aiBudget.userId = userId;
    return this.aiBudget;
  }
}