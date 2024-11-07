import { TestBed } from '@angular/core/testing';

import { ExpenditureService } from './expenditure.service';

describe('ExpenditureService', () => {
  let service: ExpenditureService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpenditureService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
