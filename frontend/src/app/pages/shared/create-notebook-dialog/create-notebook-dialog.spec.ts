import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateNotebookDialog } from './create-notebook-dialog';

describe('CreateNotebookDialog', () => {
  let component: CreateNotebookDialog;
  let fixture: ComponentFixture<CreateNotebookDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateNotebookDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateNotebookDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
