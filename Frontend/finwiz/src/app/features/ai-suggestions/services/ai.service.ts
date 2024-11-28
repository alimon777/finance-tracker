import { Injectable } from '@angular/core';
import { GoogleGenerativeAI } from '@google/generative-ai';
import { HttpClient } from '@angular/common/http';
import { Budget } from 'src/app/shared/models/budget';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AiService {

  private genAi: GoogleGenerativeAI;
  private model: any;
  private aiBudget: Budget = {
    id:0,
    userId:0,
    username: "",
    email:"",
    budgetStartDate: new Date(),
    budgetEndDate: new Date(),
    food:0,
    housing:0,
    transportation:0,
    entertainment:0,
    aiGenerated: false,
    total:0
  }

  constructor(private http: HttpClient) {
    this.genAi = new GoogleGenerativeAI(environment.googleAiApiKey);
    this.model = this.genAi.getGenerativeModel({ model: "gemini-1.5-flash" });
  }
  async generateBudgetSuggestion(transactions: any[]): Promise<string> {
    const prompt = this.createBudgetPrompt(transactions);
  
    try {
      const result = await this.model.generateContent(prompt);
      
      // Log the entire result for debugging
      // console.log("AI Response:", result);
  
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
  createBudgetPrompt(transactions: any[]): string {
    let income = 0;
    let expenses = 0;
    let foodExpenses = 0;
    let entertainmentExpenses = 0;
    let housingExpenses = 0;
    let transportationExpenses = 0;

    transactions.forEach(transaction => {
      if (transaction.transactionType === 'DEPOSIT') {
        income += transaction.amount;
      } else if (transaction.transactionType === 'WITHDRAW') {
        expenses += transaction.amount;
        if (transaction.categoryType === 'FOOD') {
          foodExpenses += transaction.amount;
        } else if (transaction.categoryType === 'ENTERTAINMENT') {
          entertainmentExpenses += transaction.amount;
        } else if (transaction.categoryType === 'HOUSING') {
          housingExpenses += transaction.amount;
        } else if (transaction.categoryType === 'TRANSPORTATION') {
          transportationExpenses += transaction.amount;
        }
      }
    });
  
    console.log('Prompt:', prompt);
    console.log('Food Expenses:', foodExpenses);
    console.log('Entertainment Expenses:', entertainmentExpenses);
    console.log('Housing Expenses:', housingExpenses);
    console.log('Transportation Expenses:', transportationExpenses);
  
    return `
      Based on the following transactions, please provide a budget suggestion:
  
      Transactions:
      ${transactions.map(t => `Transaction ID: ${t.id}, Type: ${t.transactionType}, Amount: $${t.amount}, Description: ${t.description}`).join('\n')}
  
      Total Income: $${income.toFixed(2)}
      Total Expenses: $${expenses.toFixed(2)}
      Food Expenses: $${foodExpenses.toFixed(2)}
      Entertainment Expenses: $${entertainmentExpenses.toFixed(2)}
      Housing Expenses: $${housingExpenses.toFixed(2)}
      Transportation Expenses: $${transportationExpenses.toFixed(2)}
  
      Based on these expenses, please suggest a budget plan using the following guidelines:
  
      - Please suggest new budget amounts for **Food**, **Entertainment**, **Housing**, and **Transportation** based on the current spending.
      - You can suggest an overall budget structure for each category (Food, Entertainment, Housing, Transportation) with suggested amounts based on the **current spending** and your expertise in budget planning.
      - Use <strong> tags for important details like Total Income, Total Expenses, etc.
      - Use <ul><li> tags to list out suggested budget categories.
      - Use <em> for emphasis on important categories, such as food.
      - Ensure that the sum of all budget categories (Food, Entertainment, Housing, Transportation) does not exceed the total expenses or income.
  
      Format the response like this:
      
  
        <div class="flex justify-center items-center space-x-4">
        <p>Total Income: <strong class="font-bold">$${income.toFixed(2)}</strong></p>
        <p>Total Expenses: <strong class="font-bold">$${expenses.toFixed(2)}</strong></p>
        </div>
        <br />
        <ul class=" grid-cols-2 gap-4 flex justify-center items-center space-x-4">
        <li><em>Budget for Food:</em> $[amount]</li>
        <li><em>Budget for Entertainment:</em> $[amount]</li>
        <li><em>Budget for Housing:</em> $[amount]</li>
        <li><em>Budget for Transportation:</em> $[amount]</li>
        </ul>
        <br />

      [Brief one small paragraph explanation of how the suggested budget amounts were calculated based on the current spending.]
    `;
  }

  parseAiGeneratedBudgetResponse(response: any,userId:number): Budget {
    if (!response) {
      console.error("Invalid response format:", response);
      throw new Error("AI response format is incorrect.");
    }
    const suggestedBudget: { 
      budgetStartDate: string; 
      budgetEndDate: string; 
      [key: string]: number | string;  // Add an index signature to allow dynamic property access
      food: number;
      housing: number;
      transportation: number;
      entertainment: number;
    } = {
      budgetStartDate: new Date().toISOString().split('T')[0],
      budgetEndDate: new Date().toISOString().split('T')[0],
      food: 0,
      housing: 0,
      transportation: 0,
      entertainment: 0
    };
  
    const budgetBreakdown = response || '';
  
    // Regular expression to match each category and its amount
    const categoryPattern = /Budget for (\w+):<\/em> \$(\d+(\.\d{1,2})?)/g;
    let match;
  
    // Match each category and update the suggestedBudget object
    while ((match = categoryPattern.exec(budgetBreakdown)) !== null) {
      const category = match[1].toLowerCase();  
      const amount = parseFloat(match[2]);     
  
      // Dynamically update the suggestedBudget object
      if (suggestedBudget.hasOwnProperty(category)) {
        suggestedBudget[category] = amount;
      }
    }
    this.aiBudget.aiGenerated=true;
    this.aiBudget.userId=userId;
    this.aiBudget.budgetStartDate=new Date(suggestedBudget.budgetStartDate);
    this.aiBudget.budgetEndDate=new Date(suggestedBudget.budgetEndDate);
    this.aiBudget.food=suggestedBudget.food;
    this.aiBudget.housing=suggestedBudget.housing;
    this.aiBudget.entertainment=suggestedBudget.entertainment;
    this.aiBudget.transportation=suggestedBudget.transportation;
    return this.aiBudget;
  }
}
