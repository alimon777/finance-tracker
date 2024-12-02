import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AiSuggestionComponent } from './ai-suggestion.component';

describe('AiSuggestionComponent', () => {
  let component: AiSuggestionComponent;
  let fixture: ComponentFixture<AiSuggestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AiSuggestionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AiSuggestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
