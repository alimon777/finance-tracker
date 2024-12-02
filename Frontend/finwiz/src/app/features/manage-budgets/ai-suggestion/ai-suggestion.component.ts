import { Component } from '@angular/core';

@Component({
  selector: 'app-ai-suggestion',
  templateUrl: './ai-suggestion.component.html',
  styleUrls: ['./ai-suggestion.component.css']
})
export class AiSuggestionComponent {

  isAiBudgetGenerated:boolean = false;


  refreshSuggestion():void {
    this.isAiBudgetGenerated = true;
  }

  generateSuggestion(): void {
    this.isAiBudgetGenerated = false;
  }
  
  saveBudget(){
    
  }
}
