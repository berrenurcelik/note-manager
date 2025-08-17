import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDialog } from './create-dialog';

describe('CreateDialog', () => {
  let component: CreateDialog;
  let fixture: ComponentFixture<CreateDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
