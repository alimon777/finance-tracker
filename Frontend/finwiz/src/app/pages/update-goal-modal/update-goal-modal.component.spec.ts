import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateGoalModalComponent } from './update-goal-modal.component';

describe('UpdateGoalModalComponent', () => {
  let component: UpdateGoalModalComponent;
  let fixture: ComponentFixture<UpdateGoalModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateGoalModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateGoalModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
