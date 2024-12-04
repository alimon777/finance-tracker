import { TestBed } from '@angular/core/testing';

import { AiSuggestionService } from './ai-suggestion.service';

describe('AiSuggestionService', () => {
  let service: AiSuggestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AiSuggestionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
